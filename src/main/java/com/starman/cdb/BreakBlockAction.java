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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import java.util.HashMap;
import java.util.Map;

public record BreakBlockAction(TagKey<Block> tag, int breakSteps) implements PotatoProjectileBlockHitAction {

    private static final Map<BlockPos, Integer> HIT_CACHE = new HashMap<>();

    public static final MapCodec<BreakBlockAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("tag").xmap(id -> TagKey.create(Registries.BLOCK, id), TagKey::location).forGetter(BreakBlockAction::tag),
            Codec.intRange(1, 10).optionalFieldOf("break_steps", 1).forGetter(BreakBlockAction::breakSteps)
    ).apply(instance, BreakBlockAction::new));

    @Override
    public boolean execute(LevelAccessor level, ItemStack projectile, BlockHitResult ray) {
        if (level.isClientSide()) return true;

        BlockPos pos = ray.getBlockPos();
        if (!level.getBlockState(pos).is(this.tag)) return true;

        if (level instanceof net.minecraft.world.level.Level world) {
            int currentHits = HIT_CACHE.getOrDefault(pos, 0) + 1;

            if (currentHits >= this.breakSteps) {
                level.destroyBlock(pos, true);
                HIT_CACHE.remove(pos);
                world.destroyBlockProgress(pos.hashCode(), pos, -1);
            } else {
                HIT_CACHE.put(pos, currentHits);
                int stage = (int) (((float) currentHits / (float) this.breakSteps) * 9);
                world.destroyBlockProgress(pos.hashCode(), pos, stage);
                world.levelEvent(1001, pos, Block.getId(level.getBlockState(pos)));
            }
        }
        return true;
    }

    @Override
    public MapCodec<? extends PotatoProjectileBlockHitAction> codec() {
        return CODEC;
    }
}