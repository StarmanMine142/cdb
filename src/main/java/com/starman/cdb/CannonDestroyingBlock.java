package com.starman.cdb;

import com.simibubi.create.api.registry.CreateRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(CannonDestroyingBlock.MODID)
@EventBusSubscriber(modid = CannonDestroyingBlock.MODID)
public class CannonDestroyingBlock {
    public static final String MODID = "cdb";

    public static final TagKey<Block> BREAKABLE_BY_CANNON = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(MODID, "breakable_by_cannon")
    );

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(CreateRegistries.POTATO_PROJECTILE_BLOCK_HIT_ACTION, helper -> {
            helper.register(ResourceLocation.fromNamespaceAndPath(MODID, "break_block"), BreakBlockAction.CODEC);
        });
    }
}