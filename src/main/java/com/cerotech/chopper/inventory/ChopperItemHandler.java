package com.cerotech.chopper.inventory;

import javax.annotation.Nonnull;

import com.cerotech.chopper.tileentity.ChopperTileEntity;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ChopperItemHandler extends InvWrapper {

	private final ChopperTileEntity chopperTE;

	public ChopperItemHandler(ChopperTileEntity chopperTE) {
		super(chopperTE);
		this.chopperTE = chopperTE;
	}

	@Override
	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (simulate) {
			return super.insertItem(slot, stack, simulate);
		} else {
			boolean wasEmpty = getInv().isEmpty();

			int originalStackSize = stack.getCount();
			stack = super.insertItem(slot, stack, simulate);

			if (wasEmpty && originalStackSize > stack.getCount()) {
				if (!chopperTE.mayTransfer()) {
					chopperTE.setTransferCooldown(8);
				}
			}

			return stack;
		}
	}
}
