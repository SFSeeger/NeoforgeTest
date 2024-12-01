package io.github.sfseeger.testmod.core.init.recipes;

import io.github.sfseeger.testmod.TestMod;
import io.github.sfseeger.testmod.common.recipies.toaster.ToasterCookingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RecipeSerializerInit {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, TestMod.MODID);

    public static final Supplier<RecipeSerializer<ToasterCookingRecipe>> TOASTER_COOKING_SERIALIZER =
            RECIPE_SERIALIZERS.register("toaster_cooking",
                                        () -> new SimpleCookingSerializer<>(ToasterCookingRecipe::new, 100));

}
