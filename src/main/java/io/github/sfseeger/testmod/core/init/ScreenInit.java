package io.github.sfseeger.testmod.core.init;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import io.github.sfseeger.testmod.client.screens.GeneratorScreen;

public class ScreenInit {
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MenuInit.GENERATOR_MENU.get(), GeneratorScreen::new);
    }
}
