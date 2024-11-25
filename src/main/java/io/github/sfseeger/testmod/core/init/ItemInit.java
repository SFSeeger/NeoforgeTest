package io.github.sfseeger.testmod.core.init;

import io.github.sfseeger.testmod.common.items.TestItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.sfseeger.testmod.TestMod.MODID;
import static io.github.sfseeger.testmod.core.init.BlockInit.EXAMPLE_BLOCK;

public class ItemInit {
    // Create a Deferred Register to hold Items which will all be registered under the "testmod" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // Creates a new food item with the id "testmod:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));
    public static final DeferredItem<Item> TEST_ITEM = ITEMS.registerItem("test_item", TestItem::new);

    // Creates a new BlockItem with the id "testmod:example_block", combining the namespace and path
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

}
