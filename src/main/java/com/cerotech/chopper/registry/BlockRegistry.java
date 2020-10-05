package com.cerotech.chopper.registry;

import java.util.concurrent.Callable;

import com.cerotech.chopper.CommonValues;
import com.cerotech.chopper.block.ChopperBlock;
import com.cerotech.chopper.block.ChopperVariant;
import com.cerotech.chopper.client.tileentity.ChopperItemStackRenderer;
import com.cerotech.chopper.config.ChopperConfig;
import com.cerotech.chopper.tileentity.ChopperTileEntity;
import com.progwml6.ironchest.IronChests;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {

	// REGISTRY
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			CommonValues.MOD_ID);

	// BLOCKS
	public static final RegistryObject<ChopperBlock> CHOPPER_NORMAL = register(ChopperVariant.NORMAL);

	public static final RegistryObject<ChopperBlock> CHOPPER_IRON = register(ChopperVariant.IRON);

	public static final RegistryObject<ChopperBlock> CHOPPER_GOLD = register(ChopperVariant.GOLD);

	public static final RegistryObject<ChopperBlock> CHOPPER_DIAMOND = register(ChopperVariant.DIAMOND);

	public static final RegistryObject<ChopperBlock> CHOPPER_COPPER = register(ChopperVariant.COPPER);

	public static final RegistryObject<ChopperBlock> CHOPPER_SILVER = register(ChopperVariant.SILVER);

	public static final RegistryObject<ChopperBlock> CHOPPER_CRYSTAL = register(ChopperVariant.CRYSTAL);

	public static final RegistryObject<ChopperBlock> CHOPPER_OBSIDIAN = register(ChopperVariant.OBSIDIAN);

	// REGISTER FUNCTION
	private static RegistryObject<ChopperBlock> register(ChopperVariant variant) {
		if (variant.getModID() == IronChests.MODID && !shouldRegisterIronChests()) {
			return null;
		}

		RegistryObject<ChopperBlock> block = BLOCKS.register(CommonValues.CHOPPER_BLOCK_NAME + "_" + variant.getName(),
				() -> new ChopperBlock(variant, AbstractBlock.Properties.create(Material.WOOD)
						.hardnessAndResistance(2.5F).sound(SoundType.WOOD)));

		ItemRegistry.ITEMS.register(CommonValues.CHOPPER_BLOCK_NAME + "_" + variant.getName(),
				() -> new BlockItem(block.get(), new Item.Properties().group(CommonValues.CHOPPER_ITEM_GROUP)
						.setISTER(() -> renderChopperItemStack(variant))));

		return block;
	}

	// IRON CHEST DETECTION
	private static boolean shouldRegisterIronChests() {
		return ChopperConfig.COMMON.enableIronChestsIntegration.get() && ModList.get().isLoaded(IronChests.MODID);
	}

	// ITEM STACK RENDERER
	private static Callable<ItemStackTileEntityRenderer> renderChopperItemStack(ChopperVariant variant) {
		return () -> new ChopperItemStackRenderer<ChopperTileEntity>(() -> ChopperTileEntity.createChopperTE(variant));
	}
}
