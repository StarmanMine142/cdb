package com.starman.cdb;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import java.util.Optional;

public record CannonPreset(int breakSteps, Optional<TagKey<Block>> tag, Optional<Block> block) {
    public static final Codec<CannonPreset> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.intRange(1, 10).optionalFieldOf("break_steps", 1).forGetter(CannonPreset::breakSteps),
            TagKey.codec(Registries.BLOCK).optionalFieldOf("tag").forGetter(CannonPreset::tag),
            BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("block").forGetter(CannonPreset::block)
    ).apply(instance, CannonPreset::new));
}