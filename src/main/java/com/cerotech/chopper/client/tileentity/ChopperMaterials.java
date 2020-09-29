package com.cerotech.chopper.client.tileentity;

import com.cerotech.chopper.Chopper;
import com.cerotech.chopper.block.ChopperVariant;
import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.client.tileentity.IronChestsModels;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.util.ResourceLocation;

public class ChopperMaterials {

	public static final ResourceLocation LID_TEXTURE = new ResourceLocation(Chopper.MOD_ID, "model/chopper_lid");

	public static RenderMaterial getBottomMaterial(ChopperVariant variant) {
		ResourceLocation texture;

		switch (variant.getModID()) {
//		case "quark":
//			texture = new ResourceLocation(variant.getModID(), "model/chest/" + variant.getString());
//			return new RenderMaterial(Atlases.CHEST_ATLAS, texture);
		case IronChests.MODID:
			texture = IronChestsModels.chooseChestTexture(variant.toIronChestsType());
			return new RenderMaterial(Atlases.CHEST_ATLAS, texture);
		default:
			return Atlases.CHEST_MATERIAL;
		}
	}
}
