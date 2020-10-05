package com.cerotech.chopper.registry;

import com.cerotech.chopper.CommonValues;
import com.cerotech.chopper.block.ChopperBlock;
import com.cerotech.chopper.tileentity.ChopperTileEntity;
import com.google.common.collect.Sets;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityRegistry {

	// REGISTRY
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, CommonValues.MOD_ID);

	// TILE ENTITIES
	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_NORMAL = register(
			BlockRegistry.CHOPPER_NORMAL.get());

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_IRON = register(
			BlockRegistry.CHOPPER_IRON.get());

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_GOLD = register(
			BlockRegistry.CHOPPER_GOLD.get());

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_DIAMOND = register(
			BlockRegistry.CHOPPER_DIAMOND.get());

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_COPPER = register(
			BlockRegistry.CHOPPER_COPPER.get());

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_SILVER = register(
			BlockRegistry.CHOPPER_SILVER.get());

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_CRYSTAL = register(
			BlockRegistry.CHOPPER_CRYSTAL.get());

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_OBSIDIAN = register(
			BlockRegistry.CHOPPER_OBSIDIAN.get());

	// REGISTER FUNCTION
	private static RegistryObject<TileEntityType<ChopperTileEntity>> register(ChopperBlock block) {
		switch (block.getVariant()) {
		case IRON:
			return TILE_ENTITIES.register(block.getRegistryName().getPath(),
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createIronTE, Sets.newHashSet(block),
							null));
		case GOLD:
			return TILE_ENTITIES.register(block.getRegistryName().getPath(),
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createGoldTE, Sets.newHashSet(block),
							null));
		case DIAMOND:
			return TILE_ENTITIES.register(block.getRegistryName().getPath(),
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createDiamondTE,
							Sets.newHashSet(block), null));
		case COPPER:
			return TILE_ENTITIES.register(block.getRegistryName().getPath(),
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createCopperTE,
							Sets.newHashSet(block), null));
		case SILVER:
			return TILE_ENTITIES.register(block.getRegistryName().getPath(),
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createSilverTE,
							Sets.newHashSet(block), null));
		case CRYSTAL:
			return TILE_ENTITIES.register(block.getRegistryName().getPath(),
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createCrystalTE,
							Sets.newHashSet(block), null));
		case OBSIDIAN:
			return TILE_ENTITIES.register(block.getRegistryName().getPath(),
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createObsidianTE,
							Sets.newHashSet(block), null));
		default:
			return TILE_ENTITIES.register(block.getRegistryName().getPath(),
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createNormalTE,
							Sets.newHashSet(block), null));
		}
	}
}
