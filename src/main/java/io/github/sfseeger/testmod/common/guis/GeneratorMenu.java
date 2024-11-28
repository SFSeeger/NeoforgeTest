package io.github.sfseeger.testmod.common.guis;

import io.github.sfseeger.testmod.common.blockentities.GeneratorBlockEntity;
import io.github.sfseeger.testmod.core.init.BlockInit;
import io.github.sfseeger.testmod.core.init.MenuInit;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class GeneratorMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private static final int CONTAINER_DATA_SIZE = 3;
    private final ContainerData data;

    public static final int FUEL_SLOT = 0;
    public static final int ENERGY_STORED_DATA_SLOT = 0;
    public static final int BURN_TIME_REMAINING_DATA_SLOT = 1;
    public static final int MAX_BURN_TIME_DATA_SLOT = 2;




    // Client menu constructor
    public GeneratorMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new ItemStackHandler(1), new SimpleContainerData(CONTAINER_DATA_SIZE), ContainerLevelAccess.NULL);
    }

    // Server menu constructor
    public GeneratorMenu(int containerId, Inventory playerInventory, IItemHandler handler, ContainerData data, ContainerLevelAccess access) {
        super(MenuInit.GENERATOR_MENU.get(), containerId);
        this.access = access;

        this.addSlot(new SlotItemHandler(handler, 0, 80, 39));

        checkContainerDataCount(data, CONTAINER_DATA_SIZE);
        this.addDataSlots(data);
        this.data = data;

        // Add player inventory slots
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Add player hotbar slots
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public float getEnergyProgress() {
        int stored = this.data.get(ENERGY_STORED_DATA_SLOT);
        return stored != 0 ? Mth.clamp((float) stored / (float) GeneratorBlockEntity.CAPACITY, 0.0F, 1.0F) : 0.0F;
    }

    public float getBurnProgress() {
        int remaining = this.data.get(BURN_TIME_REMAINING_DATA_SLOT);
        int maxBurnTime = this.data.get(MAX_BURN_TIME_DATA_SLOT);
        return maxBurnTime != 0 && remaining != 0 ? Mth.clamp((float) (maxBurnTime - remaining) / (float) maxBurnTime, 0.0F, 1.0F) : 0.0F;
    }

    public boolean isLit() {
        return this.data.get(BURN_TIME_REMAINING_DATA_SLOT) > 0;
    }

    public int getEnergy() {
        return this.data.get(ENERGY_STORED_DATA_SLOT);
    }

    public int getMaxEnergy() {
        return GeneratorBlockEntity.CAPACITY;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access, player, BlockInit.GENERATOR_BLOCK.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ItemStack copy = stack.copy();

            // Check if the slot is in the block's inventory (index 0 in this case)
            if (index == 0) {
                // Move from the block's inventory to the player's inventory
                if (!this.moveItemStackTo(stack, 1, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, copy);
            } else {
                // Move from the player's inventory to the block's inventory
                if (!this.moveItemStackTo(stack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == copy.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
            return copy;
        }

        return ItemStack.EMPTY;
    }
}
