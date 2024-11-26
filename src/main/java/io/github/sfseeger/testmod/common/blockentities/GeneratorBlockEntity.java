package io.github.sfseeger.testmod.common.blockentities;

import io.github.sfseeger.testmod.common.TestModEnergyStorage;
import io.github.sfseeger.testmod.common.blocks.GeneratorBlock;
import io.github.sfseeger.testmod.core.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class GeneratorBlockEntity extends BlockEntity {
    private static final int CAPACITY = 10000;
    private static final int MAX_RECEIVE = 1000;
    private static final int MAX_EXTRACT = 1000;
    private static final int ENERGY_PER_TICK = 10;

    private final TestModEnergyStorage energyStorage;
    private BlockCapability<IEnergyStorage, @Nullable Direction> energyCapability;

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            handleFuelInsertion(getStackInSlot(slot));
        }
    };

    private BlockCapability<IItemHandler, @Nullable Direction> itemCapability;
    private int burnTimeRemaining = 0;

    public GeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.GENERATOR_BLOCK_ENTITY.get(), pos, state);
        this.energyStorage = new TestModEnergyStorage(CAPACITY, MAX_RECEIVE, MAX_EXTRACT);
    }


    public IEnergyStorage getEnergyStorage(@Nullable Direction side) {
        return this.energyStorage;
    }

    public void invalidCapability() {
        if(level != null) {
            level.invalidateCapabilities(worldPosition);
        }
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return this.itemStackHandler;
    }

    public int getBurnTimeRemaining() {
        return burnTimeRemaining;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GeneratorBlockEntity blockEntity) {
        if(!level.isClientSide){
            blockEntity.tickInternal(level, pos, state);
        }
    }

    public void tickInternal(Level level, BlockPos pos, BlockState state){
        if(energyStorage.getEnergyStored() == energyStorage.getMaxEnergyStored()) {
            return;
        }

        if(burnTimeRemaining > 0){
            burnTimeRemaining--;
            energyStorage.receiveEnergy(GeneratorBlockEntity.ENERGY_PER_TICK, false);
        }
        if(burnTimeRemaining == 0 && !itemStackHandler.getStackInSlot(0).isEmpty()){
            handleFuelInsertion(itemStackHandler.getStackInSlot(0));
        }
        setChanged();
    }

    private void handleFuelInsertion(ItemStack stack) {
        if(!stack.isEmpty() && burnTimeRemaining == 0){
            int burnTime = stack.getBurnTime(null);
            if(burnTime > 0){
                burnTimeRemaining = burnTime;
                stack.shrink(1);
                setChanged();
                invalidCapability();
            }
        }
    }



    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("Energy", energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        if(pTag.contains("Energy")) {
            energyStorage.setEnergy(pTag.getInt("Energy"));
        }
    }
}