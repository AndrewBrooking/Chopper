package com.cerotech.chopper.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class DataHandler {
	
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		
		if (event.includeServer()) {
			generator.addProvider(new LootProvider(generator));
			generator.addProvider(new ChopperRecipeProvider(generator));
		}
		
		if (event.includeClient()) {
			// TODO
		}
	}
}
