package com.arcanetech.energy;

public interface IAetheriumHandler {
    int getAetheriumStored();
    int getMaxAetheriumStored();
    int receiveAetherium(int maxReceive, boolean simulate);
    int extractAetherium(int maxExtract, boolean simulate);
    boolean canReceive();
    boolean canExtract();
}