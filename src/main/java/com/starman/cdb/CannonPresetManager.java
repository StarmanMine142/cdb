package com.starman.cdb;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.HashMap;
import java.util.Map;

public class CannonPresetManager extends SimpleJsonResourceReloadListener<CannonPreset> {

    public static final Map<Identifier, CannonPreset> PRESETS = new HashMap<>();

    public CannonPresetManager() {
        super(CannonPreset.CODEC, FileToIdConverter.json("potato_projectile/presets"));
    }

    @Override
    protected void apply(Map<Identifier, CannonPreset> resources, ResourceManager manager, ProfilerFiller profiler) {
        PRESETS.clear();
        PRESETS.putAll(resources);
    }
}