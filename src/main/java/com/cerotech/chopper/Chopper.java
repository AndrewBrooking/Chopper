package com.cerotech.chopper;

import com.cerotech.chopper.client.screen.ChopperScreen;
import com.cerotech.chopper.client.tileentity.ChopperTileEntityRenderer;
import com.cerotech.chopper.config.ChopperConfig;
import com.cerotech.chopper.data.ChopperRecipeProvider;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.Atlases;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Chopper.MOD_ID)
public class Chopper {

	public static final String MOD_ID = "chopper";

	public Chopper() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ChopperConfig.COMMON_SPEC);

		modBus.addListener(this::setup);
		modBus.addListener(this::gatherData);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			modBus.addListener(this::setupClient);
			modBus.addListener(this::onStitch);
		});

		ChopperRegistry.BLOCKS.register(modBus);
		ChopperRegistry.ITEMS.register(modBus);
		ChopperRegistry.TILE_ENTITIES.register(modBus);
		ChopperRegistry.CONTAINERS.register(modBus);
	}

	private void setup(final FMLCommonSetupEvent event) {
	}

	@OnlyIn(Dist.CLIENT)
	private void setupClient(FMLClientSetupEvent event) {
		// Register screen managers
		ScreenManager.registerFactory(ChopperRegistry.CHOPPER_CONTAINER.get(), ChopperScreen::new);

		// Register TE renderers
		ClientRegistry.bindTileEntityRenderer(ChopperRegistry.CHOPPER_TE.get(), ChopperTileEntityRenderer::new);
	}

	@OnlyIn(Dist.CLIENT)
	private void onStitch(TextureStitchEvent.Pre event) {
		if (!event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
			return;
		}

		event.addSprite(ChopperTileEntityRenderer.CHOPPER_TEXTURE);
		event.addSprite(ChopperTileEntityRenderer.LID_TEXTURE);
	}

	private void gatherData(GatherDataEvent event) {
		System.out.println("Gather Data Event Received");

		if (event.includeServer()) {
			event.getGenerator().addProvider(new ChopperRecipeProvider(event.getGenerator()));
		}
	}
}
