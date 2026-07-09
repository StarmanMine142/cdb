package com.starman.cdb;

import com.simibubi.create.api.equipment.potatoCannon.PotatoCannonProjectileType;
import com.simibubi.create.api.registry.CreateRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AmethystProjectileProvider extends DatapackBuiltinEntriesProvider {
    public AmethystProjectileProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, new net.minecraft.core.RegistrySetBuilder()
                .add(CreateRegistries.POTATO_PROJECTILE_TYPE, AmethystProjectileProvider::bootstrap), Set.of("cdb"));
    }

    public static void bootstrap(net.minecraft.data.worldgen.BootstrapContext<PotatoCannonProjectileType> ctx) {
        ctx.register(
                net.minecraft.resources.ResourceKey.create(CreateRegistries.POTATO_PROJECTILE_TYPE, ResourceLocation.fromNamespaceAndPath("cdb", "amethyst_shard")),
                new PotatoCannonProjectileType.Builder()
                        .damage(6)
                        .reloadTicks(10)
                        .velocity(1.8f)
                        .knockback(0.2f)
                        .renderTumbling()
                        .addItems(Items.AMETHYST_SHARD)
                        .build()
        );
    }
}