package com.cerotech.chopper.registry;

import com.cerotech.chopper.CommonValues;
import com.cerotech.chopper.item.ChopperConverterItem;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {

	// REGISTRY
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			CommonValues.MOD_ID);

	// ITEMS
	public static final RegistryObject<ChopperConverterItem> CHOPPER_CONVERTER = ITEMS
			.register(CommonValues.CONVERTER_ITEM_NAME, () -> new ChopperConverterItem(
					new Item.Properties().group(CommonValues.CHOPPER_ITEM_GROUP).maxStackSize(16)));
}
