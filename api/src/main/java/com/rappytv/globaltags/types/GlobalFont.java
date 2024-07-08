package com.rappytv.globaltags.types;

import net.labymod.api.client.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;

public enum GlobalFont {
    DEFAULT,
    UNICODE;

    private static final Map<GlobalFont, ResourceLocation> locations = new HashMap<>();

    @Nullable
    public ResourceLocation getCachedLocation() {
        if(locations.containsKey(this)) return locations.get(this);
        locations.put(this, getResourceLocation());
        return getCachedLocation();
    }

    @Nullable
    private ResourceLocation getResourceLocation() {
        return switch (this) {
            case DEFAULT -> null;
            case UNICODE -> ResourceLocation.create(
                "minecraft",
                "uniform"
            );
            default -> ResourceLocation.create(
                "globaltags",
                this.name().toLowerCase()
            );
        };
    }
}
