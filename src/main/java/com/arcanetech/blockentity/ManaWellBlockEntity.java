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

public class ManaWellBlockEntity extends BlockEntity implements IAetheriumHandler {
    private final AetheriumStorage storage = new AetheriumStorage(5000, 100, 500);
    private int generationTick = 0;
    private static final int GENERATION_INTERVAL = 100;
    private static final int MANA_PER_TICK = 10;

    public ManaWellBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MANA_WELL_BE.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        boolean changed = false;

        generationTick++;
        if (generationTick >= GENERATION_INTERVAL) {
            generationTick = 0;
            int generated = storage.receiveAetherium(MANA_PER_TICK, false);
            if (generated > 0) changed = true;
        }

        int pushed = AetheriumNetwork.pushToNeighbors(level, pos, this, 50);
        if (pushed > 0) changed = true;

        if (changed) setChanged();
    }

    public AetheriumStorage getStorage() {
        return storage;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Aetherium", storage.getAetheriumStored());
        tag.putInt("GenerationTick", generationTick);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        storage.setAetherium(tag.getInt("Aetherium"));
        generationTick = tag.getInt("GenerationTick");
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
