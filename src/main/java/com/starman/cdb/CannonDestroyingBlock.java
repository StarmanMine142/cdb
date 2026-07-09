package com.starman.cdb;

import com.simibubi.create.content.equipment.potatoCannon.PotatoProjectileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

@Mod(CannonDestroyingBlock.MODID)
public class CannonDestroyingBlock {
    public static final String MODID = "cdb";

    public CannonDestroyingBlock() {
    }

    public static final TagKey<Block> BREAKABLE_BY_CANNON = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(MODID, "breakable_by_cannon")
    );

    @EventBusSubscriber
    public static class EventHandler {
        @SubscribeEvent
        public static void onProjectileImpact(ProjectileImpactEvent event) {
            if (!(event.getProjectile() instanceof PotatoProjectileEntity projectile)) {
                return;
            }

            if (projectile.getItem().is(net.minecraft.world.item.Items.AMETHYST_SHARD)) {
                Level level = projectile.level();
                if (level.isClientSide) return;

                var hitResult = event.getRayTraceResult();
                if (hitResult instanceof net.minecraft.world.phys.BlockHitResult blockHit) {
                    BlockPos pos = blockHit.getBlockPos();
                    BlockState state = level.getBlockState(pos);

                    if (state.is(BREAKABLE_BY_CANNON)) {
                        level.destroyBlock(pos, true);
                        projectile.discard();
                    }
                }
            }
        }
    }
}