package com.cerotech.chopper.tileentity;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import com.cerotech.chopper.Chopper;
import com.cerotech.chopper.ChopperRegistry;
import com.cerotech.chopper.block.ChopperBlock;
import com.cerotech.chopper.inventory.ChopperContainer;
import com.cerotech.chopper.inventory.ChopperItemHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleSidedInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ISidedInventoryProvider;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.VanillaInventoryCodeHooks;

public class ChopperTileEntity extends LockableLootTileEntity implements IChestLid, IHopper, ITickableTileEntity {

	/**
	 * START VARIABLES
	 */

	private NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);
	private int transferCooldown = -1;
	private long tickedGameTime;
	private int ticksSinceSync;
	protected float lidAngle;
	protected float prevLidAngle;
	protected int numPlayersUsing;
	private LazyOptional<IItemHandlerModifiable> itemHandler;

	/**
	 * END VARIABLES
	 * 
	 * START CONSTRUCTORS
	 */

	protected ChopperTileEntity(TileEntityType<?> typeIn) {
		super(typeIn);
	}

	public ChopperTileEntity() {
		this(ChopperRegistry.CHOPPER_TE.get());
	}

	/**
	 * END CONSTRUCTORS
	 * 
	 * START TICK FUNCTION
	 */

	@Override
	public void tick() {
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();

		++this.ticksSinceSync;

		this.numPlayersUsing = calculatePlayersUsingSync(this.world, this, this.ticksSinceSync, i, j, k,
				this.numPlayersUsing);

		this.prevLidAngle = this.lidAngle;

		if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
			this.playSound(SoundEvents.BLOCK_CHEST_OPEN);
		}

		if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
			float f = this.lidAngle;

			if (this.numPlayersUsing > 0) {
				this.lidAngle += 0.1F;
			} else {
				this.lidAngle -= 0.1F;
			}

			if (this.lidAngle > 1.0F) {
				this.lidAngle = 1.0F;
			}

			if (this.lidAngle < 0.5F && f >= 0.5F) {
				this.playSound(SoundEvents.BLOCK_CHEST_CLOSE);
			}

			if (this.lidAngle < 0.0F) {
				this.lidAngle = 0.0F;
			}
		}

		if (this.world != null && !this.world.isRemote) {
			--this.transferCooldown;

			this.tickedGameTime = this.world.getGameTime();

			if (!this.isOnTransferCooldown()) {
				this.setTransferCooldown(0);
				this.update(() -> {
					return this.pullItems();
				});
			}
		}
	}

	/**
	 * END TICK FUNCTION
	 * 
	 * START INVENTORY FUNCTIONS
	 */

	@Override
	public int getSizeInventory() {
		return inventory.size();
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return inventory;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> itemsIn) {
		this.inventory = itemsIn;
	}

	private boolean isFull() {
		for (ItemStack itemstack : this.inventory) {
			if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
				return false;
			}
		}

		return true;
	}

	private boolean isInventoryFull(IInventory iinventory) {
		if (iinventory.isEmpty())
			return false;

		for (int i = 0; i < iinventory.getSizeInventory(); i++) {
			ItemStack stack = iinventory.getStackInSlot(i);

			if (stack.isEmpty() || stack.getCount() < iinventory.getInventoryStackLimit()) {
				return false;
			}
		}

		return true;
	}

	private boolean transferItemsOut() {
		IInventory iinventory = this.getDestinationInventory();

		if (iinventory == null) {
			return false;
		} else {
			if (this.isInventoryFull(iinventory)) {
				return false;
			} else {
				for (int i = 0; i < this.getSizeInventory(); ++i) {
					if (!this.getStackInSlot(i).isEmpty()) {
						ItemStack itemstack = this.getStackInSlot(i).copy();
						ItemStack itemstack1 = putStackInInventoryAllSlots(this, iinventory, this.decrStackSize(i, 1));
						if (itemstack1.isEmpty()) {
							iinventory.markDirty();
							return true;
						}

						this.setInventorySlotContents(i, itemstack);
					}
				}

				return false;
			}
		}
	}

	public boolean pullItems() {
		Boolean ret = VanillaInventoryCodeHooks.extractHook(this);

		if (ret != null)
			return ret;

		IInventory iinventory = this.getSourceInventory();

		if (iinventory != null) {
			Direction direction = Direction.DOWN;

			return iinventory.isEmpty() ? false : func_213972_a(iinventory, direction).anyMatch((stack) -> {
				return pullItemFromSlot(iinventory, stack, direction);
			});
		} else {
			for (ItemEntity itementity : getCaptureItems(this)) {
				if (captureItem(this, itementity)) {
					return true;
				}
			}

			return false;
		}
	}

	private boolean pullItemFromSlot(IInventory inventoryIn, int index, Direction direction) {
		ItemStack itemstack = inventoryIn.getStackInSlot(index);

		if (!itemstack.isEmpty() && canExtractItemFromSlot(inventoryIn, itemstack, index, direction)) {
			ItemStack itemstack1 = itemstack.copy();
			ItemStack itemstack2 = putStackInInventoryAllSlots(inventoryIn, this, inventoryIn.decrStackSize(index, 1));

			if (itemstack2.isEmpty()) {
				inventoryIn.markDirty();
				return true;
			}

			inventoryIn.setInventorySlotContents(index, itemstack1);
		}

		return false;
	}

	public static ItemStack putStackInInventoryAllSlots(@Nullable IInventory source, IInventory destination,
			ItemStack stack) {
		if (destination instanceof ISidedInventory) {
			ISidedInventory isidedinventory = (ISidedInventory) destination;
			int[] aint = isidedinventory.getSlotsForFace(Direction.UP);

			for (int k = 0; k < aint.length && !stack.isEmpty(); ++k) {
				stack = insertStack(source, destination, stack, aint[k], Direction.UP);
			}
		} else {
			int i = destination.getSizeInventory();

			for (int j = 0; j < i && !stack.isEmpty(); ++j) {
				stack = insertStack(source, destination, stack, j, Direction.UP);
			}
		}

		return stack;
	}

	private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index,
			@Nullable Direction side) {
		if (!inventoryIn.isItemValidForSlot(index, stack)) {
			return false;
		} else {
			return !(inventoryIn instanceof ISidedInventory)
					|| ((ISidedInventory) inventoryIn).canInsertItem(index, stack, side);
		}
	}

	private static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, Direction side) {
		return !(inventoryIn instanceof ISidedInventory)
				|| ((ISidedInventory) inventoryIn).canExtractItem(index, stack, side);
	}

	private static ItemStack insertStack(@Nullable IInventory source, IInventory destination, ItemStack stack,
			int index, @Nullable Direction direction) {

		ItemStack itemstack = destination.getStackInSlot(index);

		if (canInsertItemInSlot(destination, stack, index, direction)) {
			boolean flag = false;
			boolean flag1 = destination.isEmpty();

			if (itemstack.isEmpty()) {
				destination.setInventorySlotContents(index, stack);
				stack = ItemStack.EMPTY;
				flag = true;
			} else if (canCombine(itemstack, stack)) {
				int i = stack.getMaxStackSize() - itemstack.getCount();
				int j = Math.min(stack.getCount(), i);
				stack.shrink(j);
				itemstack.grow(j);
				flag = j > 0;
			}

			if (flag) {
				if (flag1 && destination instanceof ChopperTileEntity) {
					ChopperTileEntity chopperTE1 = (ChopperTileEntity) destination;

					if (!chopperTE1.mayTransfer()) {
						int k = 0;
						if (source instanceof ChopperTileEntity) {
							ChopperTileEntity chopperTE2 = (ChopperTileEntity) source;
							if (chopperTE1.tickedGameTime >= chopperTE2.tickedGameTime) {
								k = 1;
							}
						}

						chopperTE1.setTransferCooldown(8 - k);
					}
				}

				destination.markDirty();
			}
		}

		return stack;
	}

	public static boolean captureItem(IInventory inv, ItemEntity itemEntity) {
		boolean flag = false;
		ItemStack itemstack = itemEntity.getItem().copy();
		ItemStack itemstack1 = putStackInInventoryAllSlots((IInventory) null, inv, itemstack);

		if (itemstack1.isEmpty()) {
			flag = true;
			itemEntity.remove();
		} else {
			itemEntity.setItem(itemstack1);
		}

		return flag;
	}

	public static List<ItemEntity> getCaptureItems(IHopper ihopper) {
		return ihopper.getCollectionArea().toBoundingBoxList().stream().flatMap((result) -> {
			return ihopper.getWorld()
					.getEntitiesWithinAABB(ItemEntity.class,
							result.offset(ihopper.getXPos() - 0.5D, ihopper.getYPos() - 0.5D, ihopper.getZPos() - 0.5D),
							EntityPredicates.IS_ALIVE)
					.stream();
		}).collect(Collectors.toList());
	}

	private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
		if (stack1.getItem() != stack2.getItem()) {
			return false;
		} else if (stack1.getDamage() != stack2.getDamage()) {
			return false;
		} else if (stack1.getCount() > stack1.getMaxStackSize()) {
			return false;
		} else {
			return ItemStack.areItemStackTagsEqual(stack1, stack2);
		}
	}

	/**
	 * END INVENTORY FUNCTIONS
	 * 
	 * START SOURCE/DESTINATION FUNCTIONS
	 */

	@Nullable
	private IInventory getSourceInventory() {
		return getInventoryAtPosition(this.getWorld(), this.pos.offset(Direction.UP));
	}

	@Nullable
	private IInventory getDestinationInventory() {
		return getInventoryAtPosition(this.getWorld(), this.pos.offset(Direction.DOWN));
	}

	@Nullable
	public static IInventory getInventoryAtPosition(World worldIn, BlockPos pos) {
		return getInventoryAtPosition(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
				(double) pos.getZ() + 0.5D);
	}

	@Nullable
	public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z) {
		IInventory iinventory = null;
		BlockPos blockpos = new BlockPos(x, y, z);
		BlockState blockstate = worldIn.getBlockState(blockpos);
		Block block = blockstate.getBlock();

		if (block instanceof ISidedInventoryProvider) {
			iinventory = ((ISidedInventoryProvider) block).createInventory(blockstate, worldIn, blockpos);
		} else if (blockstate.hasTileEntity()) {
			TileEntity tileentity = worldIn.getTileEntity(blockpos);

			if (tileentity instanceof IInventory) {
				iinventory = (IInventory) tileentity;

				if (iinventory instanceof ChestTileEntity && block instanceof ChopperBlock) {
					iinventory = ChopperBlock.getInventory((ChopperBlock) block, blockstate, worldIn, blockpos);
				}
			}
		}

		if (iinventory == null) {
			List<Entity> list = worldIn.getEntitiesInAABBexcluding((Entity) null,
					new AxisAlignedBB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D),
					EntityPredicates.HAS_INVENTORY);

			if (!list.isEmpty()) {
				iinventory = (IInventory) list.get(worldIn.rand.nextInt(list.size()));
			}
		}

		return iinventory;
	}

	/**
	 * END SOURCE/DESTINATION FUNCTIONS
	 * 
	 * START INTERACTION FUNCTIONS
	 */

	public static int getPlayersUsing(IBlockReader reader, BlockPos posIn) {
		BlockState blockstate = reader.getBlockState(posIn);

		if (blockstate.hasTileEntity()) {
			TileEntity tileentity = reader.getTileEntity(posIn);

			if (tileentity instanceof ChopperTileEntity) {
				return ((ChopperTileEntity) tileentity).numPlayersUsing;
			}
		}

		return 0;
	}

	public static int calculatePlayersUsingSync(World worldIn, LockableTileEntity te, int n1, int n2, int n3, int n4,
			int n5) {
		if (!worldIn.isRemote && n5 != 0 && (n1 + n2 + n3 + n4) % 200 == 0) {
			n5 = calculatePlayersUsing(worldIn, te, n2, n3, n4);
		}

		return n5;
	}

	public static int calculatePlayersUsing(World worldIn, LockableTileEntity te, int n1, int n2, int n3) {
		int i = 0;

		for (PlayerEntity playerentity : worldIn.getEntitiesWithinAABB(PlayerEntity.class,
				new AxisAlignedBB((double) ((float) n1 - 5.0F), (double) ((float) n2 - 5.0F),
						(double) ((float) n3 - 5.0F), (double) ((float) (n1 + 1) + 5.0F),
						(double) ((float) (n2 + 1) + 5.0F), (double) ((float) (n3 + 1) + 5.0F)))) {
			if (playerentity.openContainer instanceof ChestContainer) {
				IInventory iinventory = ((ChestContainer) playerentity.openContainer).getLowerChestInventory();
				if (iinventory == te || iinventory instanceof DoubleSidedInventory
						&& ((DoubleSidedInventory) iinventory).isPartOfLargeChest(te)) {
					++i;
				}
			}
		}

		return i;
	}

	public boolean receiveClientEvent(int id, int type) {
		if (id == 1) {
			this.numPlayersUsing = type;
			return true;
		}

		return super.receiveClientEvent(id, type);
	}

	public void openInventory(PlayerEntity player) {
		if (!player.isSpectator()) {
			if (this.numPlayersUsing < 0) {
				this.numPlayersUsing = 0;
			}

			++this.numPlayersUsing;
			this.onOpenOrClose();
		}
	}

	public void closeInventory(PlayerEntity player) {
		if (!player.isSpectator()) {
			--this.numPlayersUsing;
			this.onOpenOrClose();
		}
	}

	protected void onOpenOrClose() {
		Block block = this.getBlockState().getBlock();

		if (block instanceof ChopperBlock) {
			this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, block);
		}
	}

	/**
	 * END INTERACTION FUNCTIONS
	 * 
	 * START NBT FUNCTIONS
	 */

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);

		if (!this.checkLootAndWrite(compound)) {
			ItemStackHelper.saveAllItems(compound, this.inventory);
		}

		compound.putInt("TransferCooldown", this.transferCooldown);
		return compound;
	}

	@Override
	public void func_230337_a_(BlockState stateIn, CompoundNBT compound) {
		super.func_230337_a_(stateIn, compound);

		this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

		if (!this.checkLootAndRead(compound)) {
			ItemStackHelper.loadAllItems(compound, this.inventory);
		}

		this.transferCooldown = compound.getInt("TransferCooldown");
	}

	/**
	 * END NBT FUNCTIONS
	 * 
	 * START LOCATION FUNCTIONS
	 */

	@Override
	public double getXPos() {
		return (double) this.pos.getX() + 0.5D;
	}

	@Override
	public double getYPos() {
		return (double) this.pos.getY() + 0.5D;
	}

	@Override
	public double getZPos() {
		return (double) this.pos.getZ() + 0.5D;
	}

	/**
	 * END LOCATION FUNCTIONS
	 * 
	 * START RENDERING FUNCTIONS
	 */

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getLidAngle(float partialTicks) {
		float lidAngle = MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
		return lidAngle;
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent(Chopper.MOD_ID + ".container.chopper");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory playerInventory) {
		return ChopperContainer.createChopperContainer(id, playerInventory, this);
	}

	/**
	 * END RENDERING FUNCTIONS
	 * 
	 * START SOUND FUNCTION
	 */

	private void playSound(SoundEvent soundIn) {
		double d0 = (double) this.pos.getX() + 0.5D;
		double d1 = (double) this.pos.getY() + 0.5D;
		double d2 = (double) this.pos.getZ() + 0.5D;

		this.world.playSound((PlayerEntity) null, d0, d1, d2, soundIn, SoundCategory.BLOCKS, 0.5F,
				this.world.rand.nextFloat() * 0.1F + 0.9F);
	}

	/**
	 * END SOUND FUNCTION
	 * 
	 * START CAPABILITY FUNCTIONS
	 */

	private IItemHandlerModifiable createHandler() {
		return new ChopperItemHandler(this);
	}

	@Override
	public void updateContainingBlockInfo() {
		super.updateContainingBlockInfo();
		if (this.itemHandler != null) {
			this.itemHandler.invalidate();
			this.itemHandler = null;
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (!this.removed && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (this.itemHandler == null)
				this.itemHandler = LazyOptional.of(this::createHandler);
			return this.itemHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	/**
	 * END CAPABILITY FUNCTIONS
	 * 
	 * START TRANSFER COOLDOWN FUNCTIONS
	 */

	public void setTransferCooldown(int i) {
		this.transferCooldown = i;
	}

	private boolean isOnTransferCooldown() {
		return this.transferCooldown > 0;
	}

	public boolean mayTransfer() {
		return this.transferCooldown > 8;
	}

	/**
	 * END TRANSFER COOLDOWN FUNCTIONS
	 * 
	 * START MISC FUNCTIONS
	 */

	private boolean update(Supplier<Boolean> supplier) {
		if (this.world != null && !this.world.isRemote) {
			if (!this.isOnTransferCooldown() && this.getBlockState().get(ChopperBlock.ENABLED)) {
				boolean flag = false;

				if (!this.isEmpty()) {
					flag = this.transferItemsOut();
				}

				if (!this.isFull()) {
					flag |= supplier.get();
				}

				if (flag) {
					this.setTransferCooldown(8);
					this.markDirty();
					return true;
				}
			}

			return false;
		} else {
			return false;
		}
	}

	public long getLastUpdateTime() {
		return this.tickedGameTime;
	}

	private static IntStream func_213972_a(IInventory inv, Direction direction) {
		return inv instanceof ISidedInventory ? IntStream.of(((ISidedInventory) inv).getSlotsForFace(direction))
				: IntStream.range(0, inv.getSizeInventory());
	}

	/**
	 * END MISC FUNCTIONS
	 */
}
