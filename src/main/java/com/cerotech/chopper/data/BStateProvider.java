package com.cerotech.chopper.data;

import com.cerotech.chopper.CommonValues;
import com.cerotech.chopper.block.ChopperBlock;
import com.cerotech.chopper.registry.BlockRegistry;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder.Perspective;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class BStateProvider extends BlockStateProvider {

	public BStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, CommonValues.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		allChopperBlocks();
	}

	private void allChopperBlocks() {
		for (RegistryObject<Block> regObj : BlockRegistry.BLOCKS.getEntries()) {
			Block block = regObj.get();

			if (block != null && block instanceof ChopperBlock) {
				ChopperBlock chopperBlock = (ChopperBlock) block;
				String name = chopperBlock.getRegistryName().getPath();

				BlockModelBuilder blockBuilder = models().getBuilder("block/" + name);
				ItemModelBuilder itemBuilder = itemModels().getBuilder("item/" + name);

				// TODO: FIX
//				blockBuilder.texture("particle", chopperBlock.getVariant().getBlockTexture());

				itemBuilder.transforms().transform(Perspective.GUI).rotation(30, 45, 0).translation(0, 0, 0)
						.scale(0.625F);

				itemBuilder.transforms().transform(Perspective.GROUND).rotation(0, 0, 0).translation(0, 3, 0)
						.scale(0.25F);

				itemBuilder.transforms().transform(Perspective.HEAD).rotation(0, 180, 0).translation(0, 0, 0)
						.scale(1.0F);

				itemBuilder.transforms().transform(Perspective.FIXED).rotation(0, 180, 0).translation(0, 0, 0)
						.scale(0.5F);

				itemBuilder.transforms().transform(Perspective.THIRDPERSON_RIGHT).rotation(75, 315, 0)
						.translation(0, 2.5F, 0).scale(0.375F);

				itemBuilder.transforms().transform(Perspective.THIRDPERSON_LEFT).rotation(0, 315, 0)
						.translation(0, 0, 0).scale(0.4F);
			}
		}
	}
}
