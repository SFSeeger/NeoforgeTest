package io.github.sfseeger.testmod.common.recipies.toaster;

import io.github.sfseeger.testmod.core.init.BlockInit;
import io.github.sfseeger.testmod.core.init.recipes.RecipeSerializerInit;
import io.github.sfseeger.testmod.core.init.recipes.RecipeTypeInit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ToasterCookingRecipe extends AbstractCookingRecipe {
    public ToasterCookingRecipe(String pGroup, CookingBookCategory pCategory, Ingredient pIngredient, ItemStack pResult,
            float pExperience, int pCookingTime) {
        super(RecipeTypeInit.TOASTER_COOKING.get(), pGroup, pCategory, pIngredient, pResult, pExperience, pCookingTime);
    }

    @Override

    public ItemStack getToastSymbol() {
        return new ItemStack(BlockInit.TOASTER_BLOCK);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.TOASTER_COOKING_SERIALIZER.get();
    }
}
