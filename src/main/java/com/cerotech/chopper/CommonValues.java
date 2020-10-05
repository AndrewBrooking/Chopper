package com.cerotech.chopper;

import com.cerotech.chopper.registry.BlockRegistry;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CommonValues {

	public static final String MOD_ID = "chopper";

	public static final String CHOPPER_BLOCK_NAME = "chopper_block";
	public static final String CONVERTER_ITEM_NAME = "chopper_converter";
	public static final String CONTAINER_NAME = "chopper_container";

	/**
	 * ITEM GROUP
	 */
	public static final ItemGroup CHOPPER_ITEM_GROUP = (new ItemGroup(MOD_ID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(BlockRegistry.CHOPPER_NORMAL.get());
		}
	});
}
