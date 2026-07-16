package com.starman.cdb;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.api.equipment.potatoCannon.PotatoProjectileBlockHitAction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record BreakBlockAction(Optional<TagKey<Block>> tag, int breakSteps, List<ResourceLocation> presets) implements PotatoProjectileBlockHitAction {

    private static final Map<BlockPos, Integer> HIT_CACHE = new HashMap<>();

    public static final MapCodec<BreakBlockAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TagKey.codec(Registries.BLOCK).optionalFieldOf("tag").forGetter(BreakBlockAction::tag),
            Codec.intRange(1, 10).optionalFieldOf("break_steps", 1).forGetter(BreakBlockAction::breakSteps),
            ResourceLocation.CODEC.listOf().optionalFieldOf("presets", List.of()).forGetter(BreakBlockAction::presets)
    ).apply(instance, BreakBlockAction::new));

    @Override
    public boolean execute(LevelAccessor level, ItemStack projectile, BlockHitResult ray) {
        if (level.isClientSide()) return true;

        tag.ifPresent(t -> handle(level, ray.getBlockPos(), t, breakSteps));

        for (ResourceLocation id : presets) {
            CannonPreset preset = CannonPresetManager.PRESETS.get(id);
            if (preset != null) handle(level, ray.getBlockPos(), preset.tag(), preset.breakSteps());
        }
        return true;
    }

    private void handle(LevelAccessor level, BlockPos pos, TagKey<Block> tag, int steps) {
        if (!level.getBlockState(pos).is(tag)) return;
        if (!(level instanceof Level world)) return;

        int currentHits = HIT_CACHE.getOrDefault(pos, 0) + 1;

        if (currentHits >= steps) {
            level.destroyBlock(pos, true);
            HIT_CACHE.remove(pos);
            world.destroyBlockProgress(pos.hashCode(), pos, -1);
        } else {
            HIT_CACHE.put(pos, currentHits);
            int stage = (int) (((float) currentHits / (float) steps) * 9);
            world.destroyBlockProgress(pos.hashCode(), pos, stage);
            world.levelEvent(1001, pos, Block.getId(level.getBlockState(pos)));
        }
    }

    @Override
    public MapCodec<? extends PotatoProjectileBlockHitAction> codec() {
        return CODEC;
    }
}