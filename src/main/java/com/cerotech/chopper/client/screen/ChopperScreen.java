package com.cerotech.chopper.client.screen;

import com.cerotech.chopper.inventory.ChopperContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.progwml6.ironchest.IronChests;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChopperScreen extends ContainerScreen<ChopperContainer> implements IHasContainer<ChopperContainer> {

	public ChopperScreen(ChopperContainer container, PlayerInventory playerInv, ITextComponent title) {
		super(container, playerInv, title);

		this.xSize = container.getVariant().getXSize();
		this.ySize = container.getVariant().getYSize();
		this.playerInventoryTitleY = this.ySize - 94;
		this.passEvents = false;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX,
			int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.minecraft.getTextureManager().bindTexture(this.container.getVariant().getGuiTexture());

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		int textureXSize = this.container.getVariant().getGuiTextureSizeX();
		int textureYSize = this.container.getVariant().getGuiTextureSizeY();

		blit(matrixStack, x, y, 0, 0, this.xSize, this.ySize, textureXSize, textureYSize);

		if (this.container.getVariant().getModID() != IronChests.MODID) {
			y += this.container.getVariant().getRows() * 18 + 17;
			blit(matrixStack, x, y, 0, 126, this.xSize, 96);
		}
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
	}
}
