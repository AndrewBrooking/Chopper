package com.cerotech.chopper.client.tileentity;

import com.cerotech.chopper.ChopperRegistry;
import com.cerotech.chopper.block.ChopperBlock;
import com.cerotech.chopper.tileentity.ChopperTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class ChopperTileEntityRenderer<T extends TileEntity & IChestLid> extends TileEntityRenderer<T> {

	private static final int[] TEX_SIZE = { 64, 64 };

	private static final int[] LID_DOWN_UV = { 0, 0 };
	private static final int[] LID_NORTH_UV = { 0, 15 };
	private static final int[] LID_EAST_UV = { 0, 20 };
	private static final int[] LID_SOUTH_UV = { 0, 38 };
	private static final int[] LID_WEST_UV = { 0, 20 };
	private static final int[] BOTTOM_UV = { 0, 19 };
	private static final int[] LATCH_UV = { 0, 0 };

	private final ModelRenderer lidDown;
	private final ModelRenderer lidNorth;
	private final ModelRenderer lidEast;
	private final ModelRenderer lidSouth;
	private final ModelRenderer lidWest;
	private final ModelRenderer bottom;
	private final ModelRenderer latch;

	public ChopperTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);

		this.lidDown = new ModelRenderer(TEX_SIZE[0], TEX_SIZE[1], LID_DOWN_UV[0], LID_DOWN_UV[1]);
		this.lidDown.addBox(1.0F, 0.0F, 0.0F, 14.0F, 1.0F, 14.0F, 0.0F);
		this.lidDown.setRotationPoint(0.0F, 10.0F, 1.0F);

		this.lidNorth = new ModelRenderer(TEX_SIZE[0], TEX_SIZE[1], LID_NORTH_UV[0], LID_NORTH_UV[1]);
		this.lidNorth.addBox(2.0F, 1.0F, 0.0F, 12.0F, 4.0F, 1.0F, 0.0F);
		this.lidNorth.setRotationPoint(0.0F, 10.0F, 1.0F);

		this.lidEast = new ModelRenderer(TEX_SIZE[0], TEX_SIZE[1], LID_EAST_UV[0], LID_EAST_UV[1]);
		this.lidEast.addBox(14.0F, 1.0F, 0.0F, 1.0F, 4.0F, 14.0F, 0.0F);
		this.lidEast.setRotationPoint(0.0F, 10.0F, 1.0F);

		this.lidSouth = new ModelRenderer(TEX_SIZE[0], TEX_SIZE[1], LID_SOUTH_UV[0], LID_SOUTH_UV[1]);
		this.lidSouth.addBox(2.0F, 1.0F, 13.0F, 12.0F, 4.0F, 1.0F, 0.0F);
		this.lidSouth.setRotationPoint(0.0F, 10.0F, 1.0F);

		this.lidWest = new ModelRenderer(TEX_SIZE[0], TEX_SIZE[1], LID_WEST_UV[0], LID_WEST_UV[1]);
		this.lidWest.addBox(1.0F, 1.0F, 0.0F, 1.0F, 4.0F, 14.0F, 0.0F);
		this.lidWest.setRotationPoint(0.0F, 10.0F, 1.0F);

		this.bottom = new ModelRenderer(TEX_SIZE[0], TEX_SIZE[1], BOTTOM_UV[0], BOTTOM_UV[1]);
		this.bottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);

		this.latch = new ModelRenderer(TEX_SIZE[0], TEX_SIZE[1], LATCH_UV[0], LATCH_UV[1]);
		this.latch.addBox(7.0F, -2.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
		this.latch.setRotationPoint(0.0F, 9.0F, 0.0F);
	}

	@Override
	public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
			int combinedLightIn, int combinedOverlayIn) {

		ChopperTileEntity chopperTE = (ChopperTileEntity) tileEntityIn;

		World world = chopperTE.getWorld();
		boolean flag = world != null;
		BlockState blockstate = this.getBlockState(chopperTE, flag);
		Block block = blockstate.getBlock();

		if (block instanceof ChopperBlock) {
			ChopperBlock chopperBlock = (ChopperBlock) block;

			matrixStackIn.push();

			float f = blockstate.get(ChopperBlock.FACING).getHorizontalAngle();

			matrixStackIn.translate(0.5D, 0.5D, 0.5D);
			matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f));
			matrixStackIn.translate(-0.5D, -0.5D, -0.5D);

			TileEntityMerger.ICallbackWrapper<? extends ChopperTileEntity> callbackWrapper = this
					.getCallbackWrapper(chopperBlock, blockstate, world, chopperTE.getPos(), flag);

			float lidAngle = this.calculateLidAngle(callbackWrapper, chopperTE, partialTicks);

			int light = callbackWrapper.<Int2IntFunction>apply(new DualBrightnessCallback<>())
					.applyAsInt(combinedLightIn);

			RenderMaterial materialBottom = ChopperMaterials.getBottomMaterial(chopperBlock.getVariant());
			RenderMaterial materialLid = new RenderMaterial(Atlases.CHEST_ATLAS, ChopperMaterials.LID_TEXTURE);

			IVertexBuilder vBuilderBottom = materialBottom.getBuffer(bufferIn, RenderType::getEntityCutout);
			IVertexBuilder vBuilderLid = materialLid.getBuffer(bufferIn, RenderType::getEntityCutout);

			this.handleModelRender(matrixStackIn, vBuilderBottom, vBuilderLid, this.lidDown, this.lidNorth,
					this.lidEast, this.lidSouth, this.lidWest, this.latch, this.bottom, lidAngle, light,
					combinedOverlayIn);

			matrixStackIn.pop();
		}
	}

	private BlockState getBlockState(ChopperTileEntity chopperTE, boolean flag) {
		if (flag)
			return chopperTE.getBlockState();
		
		return (BlockState) ChopperRegistry.CHOPPER_BLOCK_NORMAL.get().getDefaultState().with(ChopperBlock.FACING,
				Direction.SOUTH);
	}

	private TileEntityMerger.ICallbackWrapper<? extends ChopperTileEntity> getCallbackWrapper(ChopperBlock blockIn,
			BlockState stateIn, World worldIn, BlockPos posIn, boolean flag) {
		if (flag)
			return blockIn.getWrapper(stateIn, worldIn, posIn);

		return TileEntityMerger.ICallback::func_225537_b_;
	}

	private float calculateLidAngle(TileEntityMerger.ICallbackWrapper<? extends ChopperTileEntity> callbackWrapper,
			ChopperTileEntity chopperTE, float pTicks) {
		float lidAngle = callbackWrapper.<Float2FloatFunction>apply(ChopperBlock.getLid(chopperTE)).get(pTicks);

		lidAngle = 1.0F - lidAngle;
		lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;
		return lidAngle;
	}

	private void handleModelRender(MatrixStack matrixStackIn, IVertexBuilder vBuilderBottom, IVertexBuilder vBuilderLid,
			ModelRenderer lidPart0, ModelRenderer lidPart1, ModelRenderer lidPart2, ModelRenderer lidPart3,
			ModelRenderer lidPart4, ModelRenderer latchModel, ModelRenderer bottomModel, float lidAngle, int lightIn,
			int overlayIn) {
		lidPart0.rotateAngleX = -(lidAngle * ((float) Math.PI / 2F));
		lidPart1.rotateAngleX = lidPart0.rotateAngleX;
		lidPart2.rotateAngleX = lidPart0.rotateAngleX;
		lidPart3.rotateAngleX = lidPart0.rotateAngleX;
		lidPart4.rotateAngleX = lidPart0.rotateAngleX;
		latchModel.rotateAngleX = lidPart0.rotateAngleX;

		lidPart0.render(matrixStackIn, vBuilderLid, lightIn, overlayIn);
		lidPart1.render(matrixStackIn, vBuilderLid, lightIn, overlayIn);
		lidPart2.render(matrixStackIn, vBuilderLid, lightIn, overlayIn);
		lidPart3.render(matrixStackIn, vBuilderLid, lightIn, overlayIn);
		lidPart4.render(matrixStackIn, vBuilderLid, lightIn, overlayIn);
		bottomModel.render(matrixStackIn, vBuilderBottom, lightIn, overlayIn);
		latchModel.render(matrixStackIn, vBuilderBottom, lightIn, overlayIn);
	}
}
