package com.starman.cdb;

import com.zurrtum.create.api.registry.CreateRegistries;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.packs.PackType;

public class CannonDestroyingBlock implements ModInitializer {
	public static final String MODID = "cdb";

	@Override
	public void onInitialize() {
		Registry.register(
				CreateRegistries.POTATO_PROJECTILE_BLOCK_HIT_ACTION,
				Identifier.fromNamespaceAndPath(MODID, "break_block"),
				BreakBlockAction.CODEC
		);
		
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(
				Identifier.fromNamespaceAndPath(MODID, "presets"),
				new CannonPresetManager()
		);
	}
}