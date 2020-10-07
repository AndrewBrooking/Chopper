package com.cerotech.chopper.registry;

import com.cerotech.chopper.CommonValues;
import com.cerotech.chopper.block.ChopperVariant;
import com.cerotech.chopper.client.tileentity.ChopperTileEntityRenderer;
import com.cerotech.chopper.tileentity.ChopperTileEntity;
import com.google.common.collect.Sets;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityRegistry {

	// REGISTRY
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, CommonValues.MOD_ID);

	// TILE ENTITIES
	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_NORMAL = register(ChopperVariant.NORMAL);

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_IRON = register(ChopperVariant.IRON);

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_GOLD = register(ChopperVariant.GOLD);

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_DIAMOND = register(ChopperVariant.DIAMOND);

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_COPPER = register(ChopperVariant.COPPER);

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_SILVER = register(ChopperVariant.SILVER);

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_CRYSTAL = register(ChopperVariant.CRYSTAL);

	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_OBSIDIAN = register(ChopperVariant.OBSIDIAN);

	// REGISTER FUNCTION
	private static RegistryObject<TileEntityType<ChopperTileEntity>> register(ChopperVariant variant) {
		String name = CommonValues.CHOPPER_BLOCK_NAME + variant.getName();

		switch (variant) {
		case IRON:
			return TILE_ENTITIES.register(name,
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createIronTE,
							Sets.newHashSet(BlockRegistry.CHOPPER_IRON.get()), null));
		case GOLD:
			return TILE_ENTITIES.register(name,
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createIronTE,
							Sets.newHashSet(BlockRegistry.CHOPPER_GOLD.get()), null));
		case DIAMOND:
			return TILE_ENTITIES.register(name,
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createIronTE,
							Sets.newHashSet(BlockRegistry.CHOPPER_DIAMOND.get()), null));
		case COPPER:
			return TILE_ENTITIES.register(name,
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createIronTE,
							Sets.newHashSet(BlockRegistry.CHOPPER_COPPER.get()), null));
		case SILVER:
			return TILE_ENTITIES.register(name,
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createIronTE,
							Sets.newHashSet(BlockRegistry.CHOPPER_SILVER.get()), null));
		case CRYSTAL:
			return TILE_ENTITIES.register(name,
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createIronTE,
							Sets.newHashSet(BlockRegistry.CHOPPER_CRYSTAL.get()), null));
		case OBSIDIAN:
			return TILE_ENTITIES.register(name,
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createIronTE,
							Sets.newHashSet(BlockRegistry.CHOPPER_OBSIDIAN.get()), null));
		default:
			return TILE_ENTITIES.register(name,
					() -> new TileEntityType<ChopperTileEntity>(ChopperTileEntity::createIronTE,
							Sets.newHashSet(BlockRegistry.CHOPPER_NORMAL.get()), null));
		}
	}
	
	public static void bindRenderers() {
		ClientRegistry.bindTileEntityRenderer(CHOPPER_NORMAL.get(), ChopperTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CHOPPER_IRON.get(), ChopperTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CHOPPER_GOLD.get(), ChopperTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CHOPPER_DIAMOND.get(), ChopperTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CHOPPER_COPPER.get(), ChopperTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CHOPPER_SILVER.get(), ChopperTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CHOPPER_CRYSTAL.get(), ChopperTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(CHOPPER_OBSIDIAN.get(), ChopperTileEntityRenderer::new);
	}
}
