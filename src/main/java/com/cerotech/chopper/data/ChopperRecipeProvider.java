package com.cerotech.chopper.data;

import java.util.function.Consumer;

import com.cerotech.chopper.Chopper;
import com.cerotech.chopper.ChopperRegistry;
import com.cerotech.chopper.config.ChopperConfig;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ChopperRecipeProvider extends RecipeProvider {

	public ChopperRecipeProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		this.addShapedRecipes(consumer);
		this.addShapelessRecipes(consumer);
	}

	private void addShapedRecipes(Consumer<IFinishedRecipe> consumer) {

		if (ChopperConfig.COMMON.enableShapedRecipe.get()) {
			ShapedRecipeBuilder.shapedRecipe(ChopperRegistry.CHOPPER_BLOCK_NORMAL.get())
				.key('I', Tags.Items.INGOTS_IRON)
				.key('C', Tags.Items.CHESTS_WOODEN)
				.patternLine("ICI")
				.patternLine("ICI")
				.patternLine(" I ")
				.addCriterion("has_hopper", hasItem(Blocks.HOPPER))
				.build(consumer, new ResourceLocation(Chopper.MOD_ID, "chopper_shaped"));
		}

		ShapedRecipeBuilder.shapedRecipe(ChopperRegistry.CHOPPER_CONVERTER.get())
			.key('S', Tags.Items.RODS_WOODEN)
			.key('H', Ingredient.fromItems(Blocks.HOPPER))
			.patternLine("SHS")
			.patternLine("S S")
			.addCriterion("has_hopper", hasItem(Blocks.HOPPER))
			.build(consumer, new ResourceLocation(Chopper.MOD_ID, "chopper_converter"));
	}

	private void addShapelessRecipes(Consumer<IFinishedRecipe> consumer) {

		if (ChopperConfig.COMMON.enableShapelessRecipe.get()) {
			ShapelessRecipeBuilder.shapelessRecipe(ChopperRegistry.CHOPPER_BLOCK_NORMAL.get())
				.addIngredient(Tags.Items.CHESTS_WOODEN)
				.addIngredient(Ingredient.fromItems(Blocks.HOPPER))
				.addCriterion("has_hopper", hasItem(Blocks.HOPPER))
				.build(consumer, new ResourceLocation(Chopper.MOD_ID, "chopper_shapeless"));
		}
	}
}
