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
	public static final ItemGroup CHOPPER_ITEM_GROUP = (new ItemGroup("chopper") {
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(CHOPPER_BLOCK.get());
		}
	});

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
	public static final RegistryObject<ChopperBlock> CHOPPER_BLOCK = BLOCKS.register("chopper_block",
			() -> new ChopperBlock(
					AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(2.5F).sound(SoundType.WOOD)));

	/**
	 * ITEMS
	 */
	public static final RegistryObject<BlockItem> CHOPPER_ITEM = ITEMS.register("chopper_block",
			() -> new BlockItem(CHOPPER_BLOCK.get(),
					new Item.Properties().group(CHOPPER_ITEM_GROUP).setISTER(() -> renderChopperItemStack())));

	public static final RegistryObject<ChopperConverterItem> CHOPPER_UPGRADE = ITEMS.register("chopper_converter",
			() -> new ChopperConverterItem(new Item.Properties().group(CHOPPER_ITEM_GROUP).maxStackSize(64)));

	/**
	 * TILE ENTITIES
	 */
	public static final RegistryObject<TileEntityType<ChopperTileEntity>> CHOPPER_TE = TILE_ENTITIES.register(
			"chopper_te",
			() -> new TileEntityType<>(ChopperTileEntity::new, Sets.newHashSet(CHOPPER_BLOCK.get()), null));

	/**
	 * CONTAINERS
	 */
	public static final RegistryObject<ContainerType<ChopperContainer>> CHOPPER_CONTAINER = CONTAINERS
			.register("chopper_container", () -> new ContainerType<>(ChopperContainer::createChopperContainer));

	/**
	 * ITEMSTACK RENDER FUNCTION
	 */
	@OnlyIn(Dist.CLIENT)
	private static Callable<ItemStackTileEntityRenderer> renderChopperItemStack() {
		return () -> new ChopperItemStackRenderer<ChopperTileEntity>(ChopperTileEntity::new);
	}
}
