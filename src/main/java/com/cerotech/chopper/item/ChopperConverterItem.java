package com.cerotech.chopper.item;

import com.cerotech.chopper.ChopperRegistry;
import com.cerotech.chopper.block.ChopperBlock;
import com.cerotech.chopper.tileentity.ChopperTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class ChopperConverterItem extends Item {

	public ChopperConverterItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		PlayerEntity entityPlayer = context.getPlayer();
		BlockPos blockPos = context.getPos();
		World world = context.getWorld();
		ItemStack itemStack = context.getItem();
		BlockState state = world.getBlockState(blockPos);
		Block block = state.getBlock();
		TileEntity te = world.getTileEntity(blockPos);

		if (world.isRemote)
			return ActionResultType.PASS;

		if (entityPlayer == null)
			return ActionResultType.PASS;

		if (!(itemStack.getItem() instanceof ChopperConverterItem))
			return ActionResultType.PASS;

		if (!(block instanceof ChestBlock))
			return ActionResultType.PASS;

		if (te instanceof ChestTileEntity) {

			if (ChestTileEntity.getPlayersUsing(world, blockPos) > 0) {
				return ActionResultType.PASS;
			}

			ChestTileEntity chestTE = (ChestTileEntity) te;

			if (!chestTE.canOpen(entityPlayer)) {
				return ActionResultType.PASS;
			}

			Direction facing = state.get(ChestBlock.FACING);
			ITextComponent customName = chestTE.getCustomName();
			NonNullList<ItemStack> chestContents = NonNullList.withSize(chestTE.getSizeInventory(), ItemStack.EMPTY);

			for (int slot = 0; slot < chestContents.size(); slot++) {
				chestContents.set(slot, chestTE.getStackInSlot(slot));
			}

			ChopperTileEntity newTE = new ChopperTileEntity();

			chestTE.updateContainingBlockInfo();

			world.removeTileEntity(blockPos);
			world.removeBlock(blockPos, false);

			BlockState newState = ChopperRegistry.CHOPPER_BLOCK.get().getDefaultState().with(ChopperBlock.FACING,
					facing);

			world.setBlockState(blockPos, newState, 3);
			world.setTileEntity(blockPos, newTE);
			world.notifyBlockUpdate(blockPos, newState, newState, 3);

			TileEntity te2 = world.getTileEntity(blockPos);

			if (te2 instanceof ChopperTileEntity) {
				if (customName != null) {
					((ChopperTileEntity) te2).setCustomName(customName);
				}

				((ChopperTileEntity) te2).setItems(chestContents);
			}

			if (!entityPlayer.abilities.isCreativeMode) {
				itemStack.shrink(1);
			}

			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}

}
