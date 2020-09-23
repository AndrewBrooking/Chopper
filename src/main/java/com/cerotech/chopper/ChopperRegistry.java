package com.cerotech.chopper;

import java.util.concurrent.Callable;

import com.cerotech.chopper.block.ChopperBlock;
import com.cerotech.chopper.client.tileentity.ChopperItemStackRenderer;
import com.cerotech.chopper.inventory.ChopperContainer;
import com.cerotech.chopper.item.ChopperConverterItem;
import com.cerotech.chopper.tileentity.ChopperTileEntity;
import com.google.common.collect.Sets;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ChopperRegistry {

	/**
	 * ITEM GROUP
	 */
	public static final ItemGroup CHOPPER_ITEM_GROUP = (new ItemGroup(Chopper.MOD_ID) {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(CHOPPER_BLOCK.get());
		}
	});

	/**
	 * REGISTRY NAMES
	 */
	public static final String CHOPPER_BLOCK_NAME = "chopper_block";
	public static final String CONVERTER_ITEM_NAME = "chopper_converter";
	public static final String CONTAINER_NAME = "chopper_container";

	/**
	 * BLOCK/ITEM PROPERTIES
	 */
	public static final AbstractBlock.Properties CHOPPER_BLOCK_PROPS = AbstractBlock.Properties.create(Material.WOOD)
			.hardnessAndResistance(2.5F).sound(SoundType.WOOD);

	public static final Item.Properties CHOPPER_ITEM_PROPS = new Item.Properties().group(CHOPPER_ITEM_GROUP)
			.setISTER(() -> renderChopperItemStack());

	public static final Item.Properties CONVERTER_ITEM_PROPS = new Item.Properties().group(CHOPPER_ITEM_GROUP)
			.maxStackSize(16);

	/**
	 * REGISTRIES
	 */
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
			Chopper.MOD_ID);

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Chopper.MOD_ID);

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister
			.create(ForgeRegistries.TILE_ENTITIES, Chopper.MOD_ID);

	public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister
			.create(ForgeRegistries.CONTAINERS, Chopper.MOD_ID);

	/**
	 * BLOCKS
	 */
	public static final RegistryObject<ChopperBlock> CHOPPER_BLOCK = BLOCKS.register(CHOPPER_BLOCK_NAME,
			() -> new ChopperBlock(CHOPPER_BLOCK_PROPS));

	/**
	 * ITEMS
	 */
	public static final RegistryObject<BlockItem> CHOPPER_ITEM = ITEMS.register(CHOPPER_BLOCK_NAME,
			() -> new BlockItem(CHOPPER_BLOCK.get(), CHOPPER_ITEM_PROPS));

	public static final RegistryObject<ChopperConverterItem> CHOPPER_CONVERTER = ITEMS.register(CONVERTER_ITEM_NAME,
			() -> new ChopperConverterItem(CONVERTER_ITEM_PROPS));

	/**
	 * TILE ENTITIES
	 */
	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_TE = TILE_ENTITIES.register(
			CHOPPER_BLOCK_NAME + "_te",
			() -> new TileEntityType<>(ChopperTileEntity::new, Sets.newHashSet(CHOPPER_BLOCK.get()), null));

	/**
	 * CONTAINERS
	 */
	public static final RegistryObject<ContainerType<ChopperContainer>> CHOPPER_CONTAINER = CONTAINERS
			.register(CONTAINER_NAME, () -> new ContainerType<>(ChopperContainer::createChopperContainer));

	/**
	 * ITEMSTACK RENDER FUNCTION
	 */
	@OnlyIn(Dist.CLIENT)
	private static Callable<ItemStackTileEntityRenderer> renderChopperItemStack() {
		return () -> new ChopperItemStackRenderer<ChopperTileEntity>(ChopperTileEntity::new);
	}
}
