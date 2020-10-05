package com.cerotech.chopper.registry;

import com.cerotech.chopper.CommonValues;
import com.cerotech.chopper.client.screen.ChopperScreen;
import com.cerotech.chopper.inventory.ChopperContainer;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerRegistry {

	// REGISTRY
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, CommonValues.MOD_ID);

	// CONTAINERS
	public static final RegistryObject<ContainerType<ChopperContainer>> CHOPPER_NORMAL = CONTAINERS.register(
			CommonValues.CONTAINER_NAME + "_normal",
			() -> new ContainerType<ChopperContainer>(ChopperContainer::createChopperContainerNormal));

	public static final RegistryObject<ContainerType<ChopperContainer>> CHOPPER_IRON = CONTAINERS.register(
			CommonValues.CONTAINER_NAME + "_iron",
			() -> new ContainerType<ChopperContainer>(ChopperContainer::createChopperContainerIron));

	public static final RegistryObject<ContainerType<ChopperContainer>> CHOPPER_GOLD = CONTAINERS.register(
			CommonValues.CONTAINER_NAME + "_gold",
			() -> new ContainerType<ChopperContainer>(ChopperContainer::createChopperContainerGold));

	public static final RegistryObject<ContainerType<ChopperContainer>> CHOPPER_DIAMOND = CONTAINERS.register(
			CommonValues.CONTAINER_NAME + "_diamond",
			() -> new ContainerType<ChopperContainer>(ChopperContainer::createChopperContainerDiamond));

	public static final RegistryObject<ContainerType<ChopperContainer>> CHOPPER_COPPER = CONTAINERS.register(
			CommonValues.CONTAINER_NAME + "_copper",
			() -> new ContainerType<ChopperContainer>(ChopperContainer::createChopperContainerCopper));

	public static final RegistryObject<ContainerType<ChopperContainer>> CHOPPER_SILVER = CONTAINERS.register(
			CommonValues.CONTAINER_NAME + "_silver",
			() -> new ContainerType<ChopperContainer>(ChopperContainer::createChopperContainerSilver));

	public static final RegistryObject<ContainerType<ChopperContainer>> CHOPPER_CRYSTAL = CONTAINERS.register(
			CommonValues.CONTAINER_NAME + "_crystal",
			() -> new ContainerType<ChopperContainer>(ChopperContainer::createChopperContainerCrystal));

	public static final RegistryObject<ContainerType<ChopperContainer>> CHOPPER_OBSIDIAN = CONTAINERS.register(
			CommonValues.CONTAINER_NAME + "_obsidian",
			() -> new ContainerType<ChopperContainer>(ChopperContainer::createChopperContainerObsidian));

	// SCREEN MANAGER REGISTRATION FUNCTION
	public static void registerScreenManagers() {
		ScreenManager.registerFactory(CHOPPER_NORMAL.get(), ChopperScreen::new);
		ScreenManager.registerFactory(CHOPPER_IRON.get(), ChopperScreen::new);
		ScreenManager.registerFactory(CHOPPER_GOLD.get(), ChopperScreen::new);
		ScreenManager.registerFactory(CHOPPER_DIAMOND.get(), ChopperScreen::new);
		ScreenManager.registerFactory(CHOPPER_COPPER.get(), ChopperScreen::new);
		ScreenManager.registerFactory(CHOPPER_SILVER.get(), ChopperScreen::new);
		ScreenManager.registerFactory(CHOPPER_CRYSTAL.get(), ChopperScreen::new);
		ScreenManager.registerFactory(CHOPPER_OBSIDIAN.get(), ChopperScreen::new);
	}
}
