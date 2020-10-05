package com.cerotech.chopper.block;

import java.util.Locale;

import com.cerotech.chopper.CommonValues;
import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.client.tileentity.IronChestsModels;
import com.progwml6.ironchest.common.block.IronChestsTypes;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

public enum ChopperVariant implements IStringSerializable {
	NORMAL("normal", CommonValues.MOD_ID),
	IRON("iron", IronChests.MODID),
	GOLD("gold", IronChests.MODID),
	DIAMOND("diamond", IronChests.MODID),
	COPPER("copper", IronChests.MODID),
	SILVER("silver", IronChests.MODID),
	CRYSTAL("crystal", IronChests.MODID),
	OBSIDIAN("obsidian", IronChests.MODID);

	private static final int DEFAULT_ROWS = 3;
	private static final int DEFAULT_COLS = 9;
	private static final int DEFAULT_SLOTS = 27;

	private static final int DEFAULT_GUI_SIZE_X = 184;
	private static final int DEFAULT_GUI_SIZE_Y = 168;

	private static final int DEFAULT_GUI_TEXTURE_X = 256;
	private static final int DEFAULT_GUI_TEXTURE_Y = 256;

	private static final ResourceLocation DEFAULT_GUI_TEXTURE = new ResourceLocation(CommonValues.MOD_ID,
			"textures/gui/chopper_gui.png");

	private static final ResourceLocation DEFAULT_BLOCK_TEXTURE = new ResourceLocation(
			"textures/entity/chest/normal.png");

	private final String name;
	private final String modid;

	ChopperVariant(String name, String modid) {
		this.name = name;
		this.modid = modid;
	}

	public String getID() {
		return this.name().toLowerCase(Locale.ROOT);
	}

	public String getModID() {
		return this.modid;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String getString() {
		return this.getName();
	}

	public IronChestsTypes toIronChestsType() {
		IronChestsTypes type;

		switch (this.getName()) {
		case "iron":
			type = IronChestsTypes.IRON;
			break;
		case "gold":
			type = IronChestsTypes.GOLD;
			break;
		case "diamond":
			type = IronChestsTypes.DIAMOND;
			break;
		case "copper":
			type = IronChestsTypes.COPPER;
			break;
		case "silver":
			type = IronChestsTypes.SILVER;
			break;
		case "crystal":
			type = IronChestsTypes.CRYSTAL;
			break;
		case "obsidian":
			type = IronChestsTypes.OBSIDIAN;
			break;
		default:
			type = IronChestsTypes.WOOD;
		}

		return type;
	}

	public int getRows() {
		if (this.getModID() == IronChests.MODID) {
			return this.toIronChestsType().getRowCount();
		}

		return DEFAULT_ROWS;
	}

	public int getCols() {
		if (this.getModID() == IronChests.MODID) {
			return this.toIronChestsType().rowLength;
		}

		return DEFAULT_COLS;
	}

	public int getSlotCount() {
		if (this.getModID() == IronChests.MODID) {
			return this.toIronChestsType().size;
		}

		return DEFAULT_SLOTS;
	}

	public int getXSize() {
		if (this.getModID() == IronChests.MODID) {
			return this.toIronChestsType().xSize;
		}

		return DEFAULT_GUI_SIZE_X;
	}

	public int getYSize() {
		if (this.getModID() == IronChests.MODID) {
			return this.toIronChestsType().ySize;
		}

		return DEFAULT_GUI_SIZE_Y;
	}

	public ResourceLocation getGuiTexture() {
		if (this.getModID() == IronChests.MODID) {
			return this.toIronChestsType().guiTexture;
		}

		return DEFAULT_GUI_TEXTURE;
	}

	public int getGuiTextureSizeX() {
		if (this.getModID() == IronChests.MODID) {
			return this.toIronChestsType().textureXSize;
		}

		return DEFAULT_GUI_TEXTURE_X;
	}

	public int getGuiTextureSizeY() {
		if (this.getModID() == IronChests.MODID) {
			return this.toIronChestsType().textureYSize;
		}

		return DEFAULT_GUI_TEXTURE_Y;
	}

	public ResourceLocation getBlockTexture() {
		if (this.getModID() == IronChests.MODID) {
			return IronChestsModels.chooseChestTexture(this.toIronChestsType());
		}

		return DEFAULT_BLOCK_TEXTURE;
	}
}
