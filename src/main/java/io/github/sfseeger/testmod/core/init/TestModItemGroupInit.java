package io.github.sfseeger.testmod.core.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.sfseeger.testmod.TestMod.MODID;

public class TestModItemGroupInit {
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "testmod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    // Creates a creative tab with the id "testmod:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("test_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.testmod")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ItemInit.TEST_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ItemInit.EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(ItemInit.EXAMPLE_BLOCK_ITEM.get());
                output.accept(ItemInit.TEST_ITEM.get());
                output.accept(ItemInit.GENERATOR_BLOCK_ITEM.get());
                output.accept(ItemInit.TOASTER_BLOCK_ITEM.get());
            }).build());
}
