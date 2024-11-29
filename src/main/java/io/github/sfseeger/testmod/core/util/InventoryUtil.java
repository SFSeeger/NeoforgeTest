package io.github.sfseeger.testmod.core.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Collections;
import java.util.List;

public class InventoryUtil {
    public static void dropItemHandlerContents(final IItemHandler inventory, Level level, BlockPos pos) {
        if(inventory == null) return;
        for(int i = 0; i < inventory.getSlots(); ++i) {
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(i)));
        }
    }

    public static List<ItemStack> getItemHandlerItemStacks(final IItemHandler inventory) {
        if(inventory == null) return Collections.EMPTY_LIST;
        List<ItemStack> itemStacks = new ObjectArrayList<>();
        for(int i = 0; i < inventory.getSlots(); ++i) {
            itemStacks.add(inventory.getStackInSlot(i));
        }
        return itemStacks;
    }
}
