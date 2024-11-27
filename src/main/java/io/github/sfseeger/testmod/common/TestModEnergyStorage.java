package io.github.sfseeger.testmod.common;

import net.neoforged.neoforge.energy.EnergyStorage;

public class TestModEnergyStorage extends EnergyStorage {
    public TestModEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public TestModEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public void setEnergy(int energy){
        this.energy = Math.min(energy, this.getMaxEnergyStored());
    }
}
