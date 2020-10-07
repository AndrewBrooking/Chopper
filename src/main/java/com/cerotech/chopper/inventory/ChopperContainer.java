package com.cerotech.chopper.inventory;

import com.cerotech.chopper.block.ChopperVariant;
import com.cerotech.chopper.registry.ContainerRegistry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ChopperContainer extends Container {

	private final ChopperVariant variant;
	private int rows, cols;

	private static final int CELL_SIZE = 18;
	private static final int PLAYER_ROWS = 3;
	private static final int PLAYER_COLS = 9;

	private static final int PADDING_X = 12;
	private static final int TOP_PADDING_Y = 18;
	private static final int BOT_PADDING_Y = 24;

	public static ChopperContainer createChopperContainerNormal(int id, PlayerInventory playerInventory) {
		return new ChopperContainer(ContainerRegistry.CHOPPER_NORMAL.get(), id,
				new Inventory(ChopperVariant.NORMAL.getSlotCount()), playerInventory, ChopperVariant.NORMAL);
	}

	public static ChopperContainer createChopperContainerIron(int id, PlayerInventory playerInventory) {
		return new ChopperContainer(ContainerRegistry.CHOPPER_IRON.get(), id,
				new Inventory(ChopperVariant.IRON.getSlotCount()), playerInventory, ChopperVariant.IRON);
	}

	public static ChopperContainer createChopperContainerGold(int id, PlayerInventory playerInventory) {
		return new ChopperContainer(ContainerRegistry.CHOPPER_GOLD.get(), id,
				new Inventory(ChopperVariant.GOLD.getSlotCount()), playerInventory, ChopperVariant.GOLD);
	}

	public static ChopperContainer createChopperContainerDiamond(int id, PlayerInventory playerInventory) {
		return new ChopperContainer(ContainerRegistry.CHOPPER_DIAMOND.get(), id,
				new Inventory(ChopperVariant.DIAMOND.getSlotCount()), playerInventory, ChopperVariant.DIAMOND);
	}

	public static ChopperContainer createChopperContainerCopper(int id, PlayerInventory playerInventory) {
		return new ChopperContainer(ContainerRegistry.CHOPPER_COPPER.get(), id,
				new Inventory(ChopperVariant.COPPER.getSlotCount()), playerInventory, ChopperVariant.COPPER);
	}

	public static ChopperContainer createChopperContainerSilver(int id, PlayerInventory playerInventory) {
		return new ChopperContainer(ContainerRegistry.CHOPPER_SILVER.get(), id,
				new Inventory(ChopperVariant.SILVER.getSlotCount()), playerInventory, ChopperVariant.SILVER);
	}

	public static ChopperContainer createChopperContainerCrystal(int id, PlayerInventory playerInventory) {
		return new ChopperContainer(ContainerRegistry.CHOPPER_CRYSTAL.get(), id,
				new Inventory(ChopperVariant.CRYSTAL.getSlotCount()), playerInventory, ChopperVariant.CRYSTAL);
	}

	public static ChopperContainer createChopperContainerObsidian(int id, PlayerInventory playerInventory) {
		return new ChopperContainer(ContainerRegistry.CHOPPER_OBSIDIAN.get(), id,
				new Inventory(ChopperVariant.OBSIDIAN.getSlotCount()), playerInventory, ChopperVariant.OBSIDIAN);
	}

	public static ChopperContainer createChopperContainer(int id, IInventory inventory, PlayerInventory playerInventory,
			ChopperVariant variant) {

		ContainerType<ChopperContainer> containerType;

		switch (variant) {
		case IRON:
			containerType = ContainerRegistry.CHOPPER_IRON.get();
		case GOLD:
			containerType = ContainerRegistry.CHOPPER_GOLD.get();
		case DIAMOND:
			containerType = ContainerRegistry.CHOPPER_DIAMOND.get();
		case COPPER:
			containerType = ContainerRegistry.CHOPPER_COPPER.get();
		case SILVER:
			containerType = ContainerRegistry.CHOPPER_SILVER.get();
		case CRYSTAL:
			containerType = ContainerRegistry.CHOPPER_CRYSTAL.get();
		case OBSIDIAN:
			containerType = ContainerRegistry.CHOPPER_OBSIDIAN.get();
		default:
			containerType = ContainerRegistry.CHOPPER_NORMAL.get();
		}

		return new ChopperContainer(containerType, id, inventory, playerInventory, variant);
	}

	private final IInventory inventory;

	protected ChopperContainer(ContainerType<?> type, int id, IInventory inventory, PlayerInventory playerInventory,
			ChopperVariant variant) {
		super(type, id);

		assertInventorySize(inventory, variant.getSlotCount());

		this.inventory = inventory;
		this.variant = variant;
		this.rows = variant.getRows();
		this.cols = variant.getCols();

		inventory.openInventory(playerInventory.player);

		// Chopper inventory slots
		this.createInventorySlots();

		// Player inventory and hotbar slots
		this.createPlayerInventorySlots(playerInventory);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return this.inventory.isUsableByPlayer(playerIn);
	}

	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		this.inventory.closeInventory(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			int totalSlots = this.rows * this.cols;
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < totalSlots) {
				if (!this.mergeItemStack(itemstack1, totalSlots, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, totalSlots, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	private void createInventorySlots() {
		for (int row = 0; row < this.rows; ++row) {
			for (int col = 0; col < this.cols; ++col) {
				this.addSlot(new Slot(this.inventory, col + row * this.cols, PADDING_X + col * CELL_SIZE,
						TOP_PADDING_Y + row * CELL_SIZE));
			}
		}
	}

	private void createPlayerInventorySlots(PlayerInventory playerInventory) {
		int startX = (this.variant.getXSize() - (CELL_SIZE * PLAYER_COLS)) / 2 + 1;

		for (int row = 0; row < PLAYER_ROWS; ++row) {
			for (int col = 0; col < PLAYER_COLS; ++col) {
				this.addSlot(new Slot(playerInventory, col + row * 9 + 9, startX + col * CELL_SIZE,
						this.getVariant().getYSize() - (4 - row) * CELL_SIZE - 10));
			}
		}

		for (int h = 0; h < PLAYER_COLS; ++h) {
			this.addSlot(
					new Slot(playerInventory, h, startX + h * CELL_SIZE, this.getVariant().getYSize() - BOT_PADDING_Y));
		}
	}

	public ChopperVariant getVariant() {
		return this.variant;
	}
}
