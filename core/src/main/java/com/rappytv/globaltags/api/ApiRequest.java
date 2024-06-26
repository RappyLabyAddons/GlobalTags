package com.rappytv.globaltags.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rappytv.globaltags.GlobalTagAddon;
import com.rappytv.globaltags.config.GlobalTagConfig;
import net.labymod.api.Laby;
import net.labymod.api.util.io.web.request.Callback;
import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Request.Method;
import net.labymod.api.util.io.web.request.types.GsonRequest;
import java.util.Map;

public abstract class ApiRequest {

    private final Gson gson = new Gson();
    private boolean successful;
    private String message;
    private String error;
    protected ResponseBody responseBody;

    private final Method method;
    private final String path;
    private final String key;
    private final boolean localized;

    public ApiRequest(Method method, String path, String key) {
        this.method = method;
        this.path = path;
        this.key = key;
        this.localized = GlobalTagConfig.localizedResponses();
    }

    public void sendAsyncRequest(Callback<JsonObject> callback) {
        GsonRequest<JsonObject> request = Request.ofGson(JsonObject.class)
            .url("https://gt.rappytv.com" + path)
            .method(method)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", key != null ? key : "")
            .addHeader("X-Addon-Version", GlobalTagAddon.version)
            .handleErrorStream()
            .async();

        Map<String, String> body = getBody();
        if(body != null)
            request.json(body);

        if(localized)
            request.addHeader("X-Minecraft-Language", Laby.labyAPI().minecraft().options().getCurrentLanguage());

        request.execute((response) -> {
            if(response.hasException()) {
                successful = false;
                error = response.exception().getMessage();
                callback.accept(response);
                return;
            }
            responseBody = gson.fromJson(response.get(), ResponseBody.class);

            if(responseBody.error != null) {
                error = responseBody.error;
                successful = false;
                callback.accept(response);
                return;
            }

            if(responseBody.message != null) this.message = responseBody.message;
            successful = true;
            callback.accept(response);
        });
    }

    public boolean isSuccessful() {
        return successful;
    }
    public String getMessage() {
        return message;
    }
    public String getError() {
        return error;
    }
    public abstract Map<String, String> getBody();
}
