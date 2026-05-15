package net.playerwin20.chang_e.datagen.providers;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.playerwin20.chang_e.registry.ModItems;

public class CERecipeProvider extends RecipeProvider {

    public CERecipeProvider(PackOutput output, CompletableFuture<Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
       SimpleCookingRecipeBuilder.smelting(
        Ingredient.of(ModItems.RAW_SILICON.get()), RecipeCategory.MISC, ModItems.SILICON_INGOT.get(),
            0.5f, 200)
            .unlockedBy("has_raw_silicon", has(ModItems.RAW_SILICON))
            .save(recipeOutput, "raw_silicon_smelting");
    }
    
}