package com.cerotech.chopper.client.screen;

import com.cerotech.chopper.inventory.ChopperContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChopperScreen extends ContainerScreen<ChopperContainer> implements IHasContainer<ChopperContainer> {

	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation(
			"textures/gui/container/generic_54.png");

	private static final int inventoryRows = 3;

	public ChopperScreen(ChopperContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);

		this.passEvents = false;
		int yOff = 114;
		this.ySize = yOff + ChopperScreen.inventoryRows * 18;
		this.playerInventoryTitleY = this.ySize - 94;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX,
			int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.minecraft.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);

		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;

		this.blit(matrixStack, i, j, 0, 0, this.xSize, ChopperScreen.inventoryRows * 18 + 17);
		this.blit(matrixStack, i, j + ChopperScreen.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.func_230459_a_(matrixStack, mouseX, mouseY);
	}
}
