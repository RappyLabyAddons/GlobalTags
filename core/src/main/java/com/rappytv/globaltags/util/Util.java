package com.rappytv.globaltags.util;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.TokenStorage.Purpose;
import net.labymod.api.labyconnect.TokenStorage.Token;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.Type;
import net.labymod.api.util.I18n;
import org.jetbrains.annotations.Nullable;

public class Util {

    public static void notify(String title, String text, boolean isError) {
        notify(
            title,
            text,
            Icon.texture(ResourceLocation.create(
                "globaltags",
                "textures/" + (isError ? "error" : "success") + ".png"
            ))
        );
    }

    public static void notify(String title, String text, Icon icon) {
        Notification.Builder builder = Notification.builder()
            .title(Component.text(title))
            .text(Component.text(text))
            .icon(icon)
            .type(Type.ADVANCEMENT);
        Laby.labyAPI().notificationController().push(builder.build());
    }

    public static boolean clearCache(boolean notify) {
        if(TagCache.isEmpty()) {
            if(notify) Util.notify(
                I18n.translate("globaltags.notifications.error"),
                I18n.translate("globaltags.notifications.cacheEmpty"),
                null
            );
            return false;
        }
        TagCache.clear();
        return true;
    }

    public static @Nullable String getSessionToken() {
        LabyConnectSession session = Laby.labyAPI().labyConnect().getSession();

        if(session == null) return null;

        Token token = session.tokenStorage().getToken(
            Purpose.JWT,
            session.self().getUniqueId()
        );

        if(token == null || token.isExpired()) return null;

        return token.getToken();
    }
}
