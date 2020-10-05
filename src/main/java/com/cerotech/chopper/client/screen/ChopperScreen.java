package com.cerotech.chopper.client.screen;

import com.cerotech.chopper.block.ChopperVariant;
import com.cerotech.chopper.inventory.ChopperContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChopperScreen extends ContainerScreen<ChopperContainer> implements IHasContainer<ChopperContainer> {

	private final ChopperVariant variant;

	public ChopperScreen(ChopperContainer container, PlayerInventory playerInv, ITextComponent title) {
		super(container, playerInv, title);

		this.variant = container.getVariant();
		this.xSize = this.variant.getXSize();
		this.ySize = this.variant.getYSize();
		this.playerInventoryTitleY = this.ySize - 94;
		this.passEvents = false;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX,
			int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.minecraft.getTextureManager().bindTexture(this.variant.getGuiTexture());

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		int textureXSize = this.variant.getGuiTextureSizeX();
		int textureYSize = this.variant.getGuiTextureSizeY();

		blit(matrixStack, x, y, 0, 0, this.xSize, this.ySize, textureXSize, textureYSize);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}
}
