package com.cerotech.chopper.inventory;

import com.cerotech.chopper.ChopperRegistry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ChopperContainer extends Container {

	private static final int ROWS = 3;
	private static final int COLS = 9;
	private static final int TOTAL_SLOTS = ROWS * COLS;
	private static final int CELL_SIZE = 18;
	private static final int LEFT_PADDING = 8;
	private static final int PLAYER_INV_START = 103;
	private static final int HOTBAR_START = 161;

	public static ChopperContainer createChopperContainer(int id, PlayerInventory playerInventory) {
		return new ChopperContainer(ChopperRegistry.CHOPPER_CONTAINER.get(), id, new Inventory(TOTAL_SLOTS),
				playerInventory);
	}

	public static ChopperContainer createChopperContainer(int id, PlayerInventory playerInventory,
			IInventory inventory) {
		return new ChopperContainer(ChopperRegistry.CHOPPER_CONTAINER.get(), id, inventory, playerInventory);
	}

	private final IInventory inventory;

	protected ChopperContainer(ContainerType<?> type, int id, IInventory inventory, PlayerInventory playerInventory) {
		super(type, id);
		this.inventory = inventory;

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
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < TOTAL_SLOTS) {
				if (!this.mergeItemStack(itemstack1, TOTAL_SLOTS, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, TOTAL_SLOTS, false)) {
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
		for (int row = 0; row < ROWS; ++row) {
			for (int col = 0; col < COLS; ++col) {
				this.addSlot(new Slot(this.inventory, col + row * COLS, LEFT_PADDING + col * CELL_SIZE,
						CELL_SIZE + row * CELL_SIZE));
			}
		}
	}

	private void createPlayerInventorySlots(PlayerInventory playerInventory) {
		int i = (ROWS - 4) * 18;

		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				this.addSlot(new Slot(playerInventory, col + row * 9 + 9, LEFT_PADDING + col * CELL_SIZE,
						PLAYER_INV_START + row * CELL_SIZE + i));
			}
		}

		for (int h = 0; h < 9; ++h) {
			this.addSlot(new Slot(playerInventory, h, LEFT_PADDING + h * CELL_SIZE, HOTBAR_START + i));
		}
	}
}
