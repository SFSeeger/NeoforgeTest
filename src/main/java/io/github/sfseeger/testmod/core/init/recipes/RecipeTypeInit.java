package io.github.sfseeger.testmod.core.init.recipes;

import io.github.sfseeger.testmod.TestMod;
import io.github.sfseeger.testmod.common.recipies.toaster.ToasterCookingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RecipeTypeInit {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE =
            DeferredRegister.create(Registries.RECIPE_TYPE, TestMod.MODID);

    public static final Supplier<RecipeType<ToasterCookingRecipe>> TOASTER_COOKING = RECIPE_TYPE.register(
            "toaster_cooking", registry_name -> new RecipeType<ToasterCookingRecipe>() {
                @Override
                public String toString() {
                    return registry_name.toString();
                }
            });
}
