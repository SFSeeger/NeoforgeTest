package io.github.sfseeger.testmod.core.init;

import io.github.sfseeger.testmod.common.guis.GeneratorMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.sfseeger.testmod.TestMod.MODID;

public class MenuInit {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MODID);


    public static final Supplier<MenuType<GeneratorMenu>> GENERATOR_MENU = MENUS.register("generator_menu", () -> new MenuType<>(GeneratorMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
