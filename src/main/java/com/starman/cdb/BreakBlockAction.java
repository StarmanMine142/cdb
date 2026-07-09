package com.starman.cdb;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.api.equipment.potatoCannon.PotatoProjectileBlockHitAction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

public record BreakBlockAction(TagKey<Block> tag) implements PotatoProjectileBlockHitAction {

    public static final MapCodec<BreakBlockAction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("tag").xmap(
                    id -> TagKey.create(Registries.BLOCK, id),
                    TagKey::location
            ).forGetter(BreakBlockAction::tag)
    ).apply(instance, BreakBlockAction::new));

    @Override
    public boolean execute(LevelAccessor level, ItemStack projectile, BlockHitResult ray) {
        if (level.isClientSide()) return true;

        var pos = ray.getBlockPos();
        if (level.getBlockState(pos).is(this.tag)) {
            level.destroyBlock(pos, true);
            return true;
        }
        return false;
    }

    @Override
    public MapCodec<? extends BreakBlockAction> codec() {
        return CODEC;
    }
}