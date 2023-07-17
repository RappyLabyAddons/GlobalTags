package com.rappytv.globaltags.api;

import com.rappytv.globaltags.util.Util;
import net.labymod.api.Laby;
import net.labymod.api.util.I18n;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class ApiHandler {

    public String getTag(UUID uuid) {
        ApiRequest request = new ApiRequest(
            "GET",
            "[TEMPORARY SESSION KEY]",
            uuid,
            null,
            ""
        );

        return request.getTag();
    }

    public void setTag(String tag) {
        ApiRequest request = new ApiRequest(
            "POST",
            "[TEMPORARY SESSION KEY]",
            Laby.labyAPI().getUniqueId(),
            tag,
            ""
        );

        if(!request.isSuccessful()) {
            Util.notify(I18n.translate("globaltags.notifications.error"), request.getError(), null);
            return;
        }
        Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage(), null);
    }

    public void resetTag() {
        ApiRequest request = new ApiRequest(
            "DELETE",
            "[TEMPORARY SESSION KEY]",
            Laby.labyAPI().getUniqueId(),
            null,
            ""
        );

        if(!request.isSuccessful()) {
            Util.notify(I18n.translate("globaltags.notifications.error"), request.getError(), null);
            return;
        }
        Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage(), null);
    }

    public void reportPlayer(UUID uuid) {
        ApiRequest request = new ApiRequest(
            "POST",
            "[TEMPORARY SESSION KEY]",
            uuid,
            null,
            "report"
        );

        if(!request.isSuccessful()) {
            Util.notify(I18n.translate("globaltags.notifications.error"), request.getError(), null);
            return;
        }
        Util.notify(I18n.translate("globaltags.notifications.success"), request.getMessage(), null);
    }
}
