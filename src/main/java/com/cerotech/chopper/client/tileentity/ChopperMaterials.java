package com.cerotech.chopper.client.tileentity;

import com.cerotech.chopper.CommonValues;
import com.cerotech.chopper.block.ChopperVariant;
import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.client.tileentity.IronChestsModels;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.util.ResourceLocation;

public class ChopperMaterials {

	public static final ResourceLocation LID_TEXTURE = new ResourceLocation(CommonValues.MOD_ID, "model/chopper_lid");

	public static RenderMaterial getBottomMaterial(ChopperVariant variant) {
		ResourceLocation texture;

		switch (variant.getModID()) {
		case IronChests.MODID:
			texture = IronChestsModels.chooseChestTexture(variant.toIronChestsType());
			return new RenderMaterial(Atlases.CHEST_ATLAS, texture);
		default:
			return Atlases.CHEST_MATERIAL;
		}
	}
}
