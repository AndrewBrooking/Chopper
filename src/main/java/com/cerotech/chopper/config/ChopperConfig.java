package com.cerotech.chopper.config;

import org.apache.commons.lang3.tuple.Pair;

import com.cerotech.chopper.Chopper;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = Chopper.MOD_ID, bus = Bus.MOD)
public class ChopperConfig {

	public static class Common {

		public final BooleanValue enableShapelessRecipe;
		public final BooleanValue enableShapedRecipe;

//		public final BooleanValue enableQuarkIntegration;
		public final BooleanValue enableIronChestsIntegration;

		public Common(ForgeConfigSpec.Builder builder) {

			builder.push("recipes");

			enableShapelessRecipe = builder.comment("Allow a Chopper to be crafted using the SHAPELESS recipe.")
					.define("enableShapelessRecipe", true);

			enableShapedRecipe = builder.comment("Allow a Chopper to be crafted using the SHAPED recipe.")
					.define("enableShapedRecipe", true);

			builder.pop();

			builder.push("integration");

//			enableQuarkIntegration = builder
//					.comment(
//							"Adds all wooden chest variants from Quark. If Quark is not installed this wil do nothing.")
//					.define("enableQuarkIntegration", true);

			enableIronChestsIntegration = builder.comment(
					"Adds all chest variants from Iron Chests. If Iron Chests is not installed this will do nothing.")
					.define("enableIronChestsIntegration", true);

			builder.pop();
		}
	}

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = commonPair.getRight();
		COMMON = commonPair.getLeft();
	}
	
	public static boolean getValueFromName(String name) {
		switch(name) {
		case "enableShapelessRecipe":
			return COMMON.enableShapelessRecipe.get().booleanValue();
		case "enableShapedRecipe":
			return COMMON.enableShapedRecipe.get().booleanValue();
//		case "enableQuarkIntegration":
//			return COMMON.enableQuarkIntegration.get().booleanValue();
		case "enableIronChestsIntegration":
			return COMMON.enableIronChestsIntegration.get().booleanValue();
		default:
			return false;
		}
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading event) {
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfig.Reloading event) {
	}
}
