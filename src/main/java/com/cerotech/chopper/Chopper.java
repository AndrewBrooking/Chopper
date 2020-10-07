package com.cerotech.chopper;

import com.cerotech.chopper.client.tileentity.ChopperMaterials;
import com.cerotech.chopper.config.ChopperConfig;
import com.cerotech.chopper.data.DataHandler;
import com.cerotech.chopper.registry.BlockRegistry;
import com.cerotech.chopper.registry.ContainerRegistry;
import com.cerotech.chopper.registry.ItemRegistry;
import com.cerotech.chopper.registry.TileEntityRegistry;

import net.minecraft.client.renderer.Atlases;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CommonValues.MOD_ID)
public class Chopper {

	public Chopper() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ChopperConfig.COMMON_SPEC);

		modBus.addListener(this::setupCommon);
		modBus.addListener(DataHandler::gatherData);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			modBus.addListener(this::setupClient);
			modBus.addListener(this::onStitch);
		});

		BlockRegistry.BLOCKS.register(modBus);
		ItemRegistry.ITEMS.register(modBus);
		TileEntityRegistry.TILE_ENTITIES.register(modBus);
		ContainerRegistry.CONTAINERS.register(modBus);
	}

	private void setupCommon(final FMLCommonSetupEvent event) {
	}

	private void setupClient(final FMLClientSetupEvent event) {
		// Register screen managers
		ContainerRegistry.registerScreenManagers();

		// Register TE renderers
		TileEntityRegistry.bindRenderers();
	}

	private void onStitch(TextureStitchEvent.Pre event) {
		if (!event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
			return;
		}

		event.addSprite(ChopperMaterials.LID_TEXTURE);
	}
}
