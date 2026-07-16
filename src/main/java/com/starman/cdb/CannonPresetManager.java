package com.starman.cdb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.HashMap;
import java.util.Map;

public class CannonPresetManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();
    public static final Map<ResourceLocation, CannonPreset> PRESETS = new HashMap<>();

    public CannonPresetManager() {
        super(GSON, "create/potato_projectile/presets");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, ProfilerFiller profiler) {
        PRESETS.clear();
        resources.forEach((id, json) -> {
            CannonPreset.CODEC.parse(JsonOps.INSTANCE, json).result().ifPresent(preset -> PRESETS.put(id, preset));
        });
    }
}