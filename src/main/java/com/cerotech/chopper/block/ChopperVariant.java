package com.cerotech.chopper.block;

import java.util.Locale;

import com.progwml6.ironchest.common.block.IronChestsTypes;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

public enum ChopperVariant implements IStringSerializable {
	NORMAL("normal", "chopper"),
	IRON("iron", "ironchest"),
	GOLD("gold", "ironchest"),
	DIAMOND("diamond", "ironchest"),
	COPPER("copper", "ironchest"),
	SILVER("silver", "ironchest"),
	CRYSTAL("crystal", "ironchest"),
	OBSIDIAN("obsidian", "ironchest");

	private final String NAME;
	private final String MODID;

	ChopperVariant(String name, String modid) {
		this.NAME = name;
		this.MODID = modid;
	}

	public String getID() {
		return this.name().toLowerCase(Locale.ROOT);
	}

	public String getModID() {
		return this.MODID;
	}

	public String getName() {
		return this.NAME;
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
		switch (this.getModID()) {
		case "ironchest":
			return this.toIronChestsType().getRowCount();
		default:
			return 3;
		}
	}

	public int getCols() {
		switch (this.getModID()) {
		case "ironchest":
			return this.toIronChestsType().rowLength;
		default:
			return 9;
		}
	}

	public int getXSize() {
		switch (this.getModID()) {
		case "ironchest":
			return this.toIronChestsType().xSize;
		default:
			return 176;
		}
	}

	public int getYSize() {
		switch (this.getModID()) {
		case "ironchest":
			return this.toIronChestsType().ySize;
		default:
			return 168;
		}
	}

	public int getXPadding() {
		switch (this.getModID()) {
		case "ironchest":
			return 12;
		default:
			return 8;
		}
	}

	public ResourceLocation getGuiTexture() {
		switch (this.getModID()) {
		case "ironchest":
			return this.toIronChestsType().guiTexture;
		default:
			return new ResourceLocation("textures/gui/container/generic_54.png");
		}
	}
	
	public int getGuiTextureSizeX() {
		switch (this.getModID()) {
		case "ironchest":
			return this.toIronChestsType().textureXSize;
		default:
			return 256;
		}
	}
	
	public int getGuiTextureSizeY() {
		switch (this.getModID()) {
		case "ironchest":
			return this.toIronChestsType().textureYSize;
		default:
			return 168;
		}
	}
}
