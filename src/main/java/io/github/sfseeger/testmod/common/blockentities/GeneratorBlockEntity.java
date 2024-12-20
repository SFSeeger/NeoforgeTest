package io.github.sfseeger.testmod.common.blockentities;

import io.github.sfseeger.testmod.common.TestModEnergyStorage;
import io.github.sfseeger.testmod.common.blocks.GeneratorBlock;
import io.github.sfseeger.testmod.common.guis.GeneratorMenu;
import io.github.sfseeger.testmod.core.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class GeneratorBlockEntity extends BlockEntity {
    public static final int CAPACITY = 10000;
    private static final int MAX_RECEIVE = 1000;
    private static final int MAX_EXTRACT = 1000;
    private static final int ENERGY_PER_TICK = 10;

    private int maxBurnTime = 0;
    private int burnTimeRemaining = 0;

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> energyStorage.getEnergyStored();
                case 1 -> burnTimeRemaining;
                case 2 -> maxBurnTime;
                default -> 0;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0 -> energyStorage.setEnergy(pValue);
                case 1 -> burnTimeRemaining = pValue;
                case 2 -> maxBurnTime = pValue;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    private final TestModEnergyStorage energyStorage;

    public IEnergyStorage getEnergyStorage(@Nullable Direction side) {
        return this.energyStorage;
    }

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            handleFuelInsertion(getStackInSlot(slot));
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getBurnTime(null) > 0;
        }
    };

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return this.itemStackHandler;
    }

    public GeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.GENERATOR_BLOCK_ENTITY.get(), pos, state);
        this.energyStorage = new TestModEnergyStorage(CAPACITY, MAX_RECEIVE, MAX_EXTRACT);
    }

    public void invalidCapability() {
        if (level != null) {
            level.invalidateCapabilities(worldPosition);
        }
    }


    public int getBurnTimeRemaining() {
        return burnTimeRemaining;
    }

    public int getMaxBurnTime() {
        return maxBurnTime;
    }

    public boolean isLit() {
        return burnTimeRemaining > 0;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GeneratorBlockEntity blockEntity) {
        if (!level.isClientSide) {
            blockEntity.tickInternal(level, pos, state);
        }
    }

    public void tickInternal(Level level, BlockPos pos, BlockState state) {
        if (energyStorage.getEnergyStored() == energyStorage.getMaxEnergyStored()) {
            return;
        }
        if (burnTimeRemaining > 0) {
            burnTimeRemaining--;
            energyStorage.receiveEnergy(GeneratorBlockEntity.ENERGY_PER_TICK, false);
        }
        if (burnTimeRemaining == 0 && !itemStackHandler.getStackInSlot(0).isEmpty()) {
            handleFuelInsertion(itemStackHandler.getStackInSlot(0));
        }
        level.setBlock(pos, state.setValue(GeneratorBlock.LIT, isLit()), 3);
        setChanged();
    }

    private void handleFuelInsertion(ItemStack stack) {
        if (!stack.isEmpty() && burnTimeRemaining == 0) {
            int burnTime = stack.getBurnTime(null);
            if (burnTime > 0) {
                burnTimeRemaining = maxBurnTime = burnTime;
                stack.shrink(1);
                invalidCapability();
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("Energy", energyStorage.getEnergyStored());
        CompoundTag fuelTag = new CompoundTag();
        fuelTag.putInt("BurnTime", burnTimeRemaining);
        fuelTag.putInt("MaxBurnTime", maxBurnTime);
        pTag.put("Fuel", fuelTag);
        pTag.put("Inventory", itemStackHandler.serializeNBT(pRegistries));
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        energyStorage.setEnergy(pTag.getInt("Energy"));
        if (pTag.contains("Inventory")) {
            itemStackHandler.deserializeNBT(pRegistries, pTag.getCompound("Inventory"));
        }
        if (pTag.contains("Fuel")) {
            CompoundTag fuelTag = pTag.getCompound("Fuel");
            burnTimeRemaining = fuelTag.getInt("BurnTime");
            maxBurnTime = fuelTag.getInt("MaxBurnTime") == 0 ? burnTimeRemaining : fuelTag.getInt("MaxBurnTime");
        }
    }

    public AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
        return new GeneratorMenu(pId, pPlayer, this.itemStackHandler, this.dataAccess, ContainerLevelAccess.NULL);
    }
}
