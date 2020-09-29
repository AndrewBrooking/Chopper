package com.cerotech.chopper.inventory;

import com.cerotech.chopper.ChopperRegistry;
import com.cerotech.chopper.block.ChopperVariant;

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

	private static final int DEFAULT_SIZE = 27;
	private static final int CELL_SIZE = 18;
	private static final int PLAYER_ROWS = 3;
	private static final int PLAYER_COLS = 9;
	private static final int HOTBAR_OFFSET = 24;
	private static final int HOTBAR_Y_PADDING = 4;

	public static ChopperContainer createChopperContainer(int id, PlayerInventory playerInventory) {
		return new ChopperContainer(ChopperRegistry.CHOPPER_CONTAINER.get(), id, new Inventory(DEFAULT_SIZE),
				playerInventory, ChopperVariant.NORMAL);
	}

	public static ChopperContainer createChopperContainer(int id, IInventory inventory,
			PlayerInventory playerInventory) {
		return new ChopperContainer(ChopperRegistry.CHOPPER_CONTAINER.get(), id, inventory, playerInventory,
				ChopperVariant.NORMAL);
	}

	public static ChopperContainer createChopperContainer(int id, IInventory inventory, PlayerInventory playerInventory,
			ChopperVariant variant) {
		return new ChopperContainer(ChopperRegistry.CHOPPER_CONTAINER.get(), id, inventory, playerInventory, variant);
	}

	private final IInventory inventory;

	protected ChopperContainer(ContainerType<?> type, int id, IInventory inventory, PlayerInventory playerInventory,
			ChopperVariant variant) {
		super(type, id);
		this.inventory = inventory;
		this.variant = variant;

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
			int totalSlots = this.variant.getRows() * this.variant.getCols();
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
		int rows = this.variant.getRows();
		int cols = this.variant.getCols();

		for (int row = 0; row < rows; ++row) {
			for (int col = 0; col < cols; ++col) {
				this.addSlot(new Slot(this.inventory, col + row * cols, this.variant.getXPadding() + col * CELL_SIZE,
						CELL_SIZE + row * CELL_SIZE));
			}
		}
	}

	private void createPlayerInventorySlots(PlayerInventory playerInventory) {
		int xPadding = (this.variant.getXSize() - (CELL_SIZE * PLAYER_COLS)) / 2 + 1;

		for (int row = 0; row < PLAYER_ROWS; ++row) {
			for (int col = 0; col < PLAYER_COLS; ++col) {
				this.addSlot(new Slot(playerInventory, col + row * 9 + 9, xPadding + col * CELL_SIZE,
						this.variant.getYSize() - (HOTBAR_OFFSET + HOTBAR_Y_PADDING) - (row * CELL_SIZE)));
			}
		}

		for (int h = 0; h < 9; ++h) {
			this.addSlot(
					new Slot(playerInventory, h, xPadding + h * CELL_SIZE, this.variant.getYSize() - HOTBAR_OFFSET));
		}
	}

	public ChopperVariant getVariant() {
		return this.variant;
	}
}
