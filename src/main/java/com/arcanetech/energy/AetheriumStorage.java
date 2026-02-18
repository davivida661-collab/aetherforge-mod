package com.arcanetech.energy;

public class AetheriumStorage implements IAetheriumHandler {
    protected int aetherium;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public AetheriumStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public AetheriumStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public AetheriumStorage(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public AetheriumStorage(int capacity, int maxReceive, int maxExtract, int aetherium) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.aetherium = Math.max(0, Math.min(capacity, aetherium));
    }

    @Override
    public int getAetheriumStored() {
        return aetherium;
    }

    @Override
    public int getMaxAetheriumStored() {
        return capacity;
    }

    @Override
    public int receiveAetherium(int maxReceive, boolean simulate) {
        if (!canReceive()) return 0;
        int energyReceived = Math.min(capacity - aetherium, Math.min(this.maxReceive, maxReceive));
        if (!simulate) aetherium += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractAetherium(int maxExtract, boolean simulate) {
        if (!canExtract()) return 0;
        int energyExtracted = Math.min(aetherium, Math.min(this.maxExtract, maxExtract));
        if (!simulate) aetherium -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    public void setAetherium(int amount) {
        this.aetherium = Math.max(0, Math.min(capacity, amount));
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        if (aetherium > capacity) aetherium = capacity;
    }
}