package com.arcanetech.blockentity;

import com.arcanetech.energy.AetheriumNetwork;
import com.arcanetech.energy.AetheriumStorage;
import com.arcanetech.energy.IAetheriumHandler;
import com.arcanetech.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AetherCapacitorBlockEntity extends BlockEntity implements IAetheriumHandler {
    private final AetheriumStorage storage = new AetheriumStorage(100000, 1000, 1000);
    private static final int TRANSFER_RATE = 250;

    public AetherCapacitorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AETHER_CAPACITOR_BE.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        boolean changed = false;

        int pulled = AetheriumNetwork.pullFromNeighbors(level, pos, this, TRANSFER_RATE);
        int pushed = AetheriumNetwork.pushToNeighbors(level, pos, this, TRANSFER_RATE);

        if (pulled > 0 || pushed > 0) {
            changed = true;
        }

        if (changed) setChanged();
    }

    public AetheriumStorage getStorage() {
        return storage;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Aetherium", storage.getAetheriumStored());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        storage.setAetherium(tag.getInt("Aetherium"));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public int getAetheriumStored() {
        return storage.getAetheriumStored();
    }

    @Override
    public int getMaxAetheriumStored() {
        return storage.getMaxAetheriumStored();
    }

    @Override
    public int receiveAetherium(int maxReceive, boolean simulate) {
        return storage.receiveAetherium(maxReceive, simulate);
    }

    @Override
    public int extractAetherium(int maxExtract, boolean simulate) {
        return storage.extractAetherium(maxExtract, simulate);
    }

    @Override
    public boolean canReceive() {
        return storage.canReceive();
    }

    @Override
    public boolean canExtract() {
        return storage.canExtract();
    }
}
