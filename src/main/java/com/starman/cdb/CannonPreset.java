package com.starman.cdb;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public record CannonPreset(int breakSteps, TagKey<Block> tag) {
    public static final Codec<CannonPreset> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("break_steps").forGetter(CannonPreset::breakSteps),
            ResourceLocation.CODEC.fieldOf("tag").xmap(id -> TagKey.create(Registries.BLOCK, id), TagKey::location).forGetter(CannonPreset::tag)
    ).apply(instance, CannonPreset::new));
}