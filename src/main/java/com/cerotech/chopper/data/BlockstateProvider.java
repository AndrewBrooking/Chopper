package com.cerotech.chopper.data;

import com.cerotech.chopper.Chopper;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockstateProvider extends BlockStateProvider {

	public BlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Chopper.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		// TODO
	}
}
