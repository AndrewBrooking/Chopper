package com.cerotech.chopper.block;

import java.util.Optional;
import java.util.function.BiPredicate;

import javax.annotation.Nullable;

import com.cerotech.chopper.ChopperRegistry;
import com.cerotech.chopper.tileentity.ChopperTileEntity;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.DoubleSidedInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ChopperBlock extends ContainerBlock implements IWaterLoggable {

	/**
	 * BEGIN VARIABLES
	 */

	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

	protected static final VoxelShape CHEST_SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);

	private static final TileEntityMerger.ICallback<ChopperTileEntity, Optional<IInventory>> tileEntityMerger = new TileEntityMerger.ICallback<ChopperTileEntity, Optional<IInventory>>() {

		public Optional<IInventory> func_225539_a_(ChopperTileEntity te1, ChopperTileEntity te2) {
			return Optional.of(new DoubleSidedInventory(te1, te2));
		}

		public Optional<IInventory> func_225538_a_(ChopperTileEntity te) {
			return Optional.of(te);
		}

		public Optional<IInventory> func_225537_b_() {
			return Optional.empty();
		}
	};

	/**
	 * END VARIABLES
	 * 
	 * BEGIN CONSTRUCTOR
	 */

	public ChopperBlock(Properties properties) {
		super(properties);

		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH)
				.with(WATERLOGGED, Boolean.valueOf(false)).with(ENABLED, Boolean.valueOf(true)));
	}

	/**
	 * END CONSTRUCTOR
	 * 
	 * BEGIN RENDER FUNCTIONS
	 */

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return CHEST_SHAPE;
	}

	@OnlyIn(Dist.CLIENT)
	public static TileEntityMerger.ICallback<ChopperTileEntity, Float2FloatFunction> getLid(
			final ChopperTileEntity chopperTE) {
		return new TileEntityMerger.ICallback<ChopperTileEntity, Float2FloatFunction>() {

			public Float2FloatFunction func_225539_a_(ChopperTileEntity te1, ChopperTileEntity te2) {
				return (pTicks) -> {
					return Math.max(te1.getLidAngle(pTicks), te2.getLidAngle(pTicks));
				};
			}

			public Float2FloatFunction func_225538_a_(ChopperTileEntity te) {
				return te::getLidAngle;
			}

			public Float2FloatFunction func_225537_b_() {
				return chopperTE::getLidAngle;
			}
		};
	}

	/**
	 * END RENDER FUNCTIONS
	 * 
	 * BEGIN TILE ENTITY FUNCTIONS
	 */

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new ChopperTileEntity();
	}

	@Override
	@Nullable
	public INamedContainerProvider getContainer(BlockState state, World world, BlockPos pos) {
		TileEntity tileentity = world.getTileEntity(pos);
		return tileentity instanceof ChopperTileEntity ? (ChopperTileEntity) tileentity : null;
	}

	/**
	 * END TILE ENTITY FUNCTIONS
	 * 
	 * BEGIN BLOCK STATE FUNCTIONS
	 */

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction facing = context.getPlacementHorizontalFacing().getOpposite();
		boolean isWater = context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER;

		return this.getDefaultState().with(FACING, facing).with(WATERLOGGED, isWater).with(ENABLED,
				Boolean.valueOf(true));
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return stateIn;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof ChopperTileEntity) {
				((ChopperTileEntity) tileentity).setCustomName(stack.getDisplayName());
			}
		}
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.isIn(newState.getBlock())) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof IInventory) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (!oldState.isIn(state.getBlock())) {
			this.updateState(worldIn, pos, state);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		this.updateState(worldIn, pos, state);
	}

	private void updateState(World worldIn, BlockPos pos, BlockState state) {
		boolean flag = !worldIn.isBlockPowered(pos);
		if (flag != state.get(ENABLED)) {
			worldIn.setBlockState(pos, state.with(ENABLED, Boolean.valueOf(flag)), 4);
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED, ENABLED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
	}

	/**
	 * END BLOCK STATE FUNCTIONS
	 * 
	 * BEGIN INTERACTION FUNCTIONS
	 */

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isRemote)
			return ActionResultType.SUCCESS;

		INamedContainerProvider container = this.getContainer(state, worldIn, pos);

		if (container != null) {
			player.openContainer(container);
			player.addStat(this.getOpenStat());
			PiglinTasks.func_234478_a_(player, true);
		}

		return ActionResultType.CONSUME;
	}

	/**
	 * END INTERACTION FUNCTIONS
	 * 
	 * BEGIN REDSTONE FUNCTIONS
	 */

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return Container.calcRedstoneFromInventory((IInventory) worldIn.getTileEntity(pos));
	}

	/**
	 * END REDSTONE FUNCTIONS
	 * 
	 * BEGIN MISC FUNCTIONS
	 */

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	protected Stat<ResourceLocation> getOpenStat() {
		return Stats.CUSTOM.get(Stats.OPEN_CHEST);
	}

	@Nullable
	public static IInventory getInventory(ChopperBlock blockIn, BlockState stateIn, World worldIn, BlockPos posIn) {
		return blockIn.getWrapper(stateIn, worldIn, posIn).<Optional<IInventory>>apply(tileEntityMerger)
				.orElse((IInventory) null);
	}

	public TileEntityMerger.ICallbackWrapper<? extends ChopperTileEntity> getWrapper(BlockState blockState, World world,
			BlockPos blockPos) {

		BiPredicate<IWorld, BlockPos> biPredicate = (p_226918_0_, p_226918_1_) -> false;

		return TileEntityMerger.func_226924_a_(ChopperRegistry.CHOPPER_TE.get(), ChopperBlock::getMergerType,
				ChopperBlock::getDirectionToAttached, FACING, blockState, world, blockPos, biPredicate);
	}

	public static TileEntityMerger.Type getMergerType(BlockState blockState) {
		return TileEntityMerger.Type.SINGLE;
	}

	public static Direction getDirectionToAttached(BlockState state) {
		Direction direction = state.get(FACING);
		return direction.rotateYCCW();
	}

	/**
	 * END MISC FUNCTIONS
	 */
}
