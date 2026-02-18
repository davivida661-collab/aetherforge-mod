package com.arcanetech.blockentity;

import com.arcanetech.energy.AetheriumNetwork;
import com.arcanetech.energy.AetheriumStorage;
import com.arcanetech.energy.IAetheriumHandler;
import com.arcanetech.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class FluxReactorBlockEntity extends BlockEntity implements IAetheriumHandler {
    private final AetheriumStorage storage = new AetheriumStorage(20000, 500, 2000);
    private final ItemStackHandler itemHandler = new ItemStackHandler(1);
    private int burnTime = 0;
    private static final int FLUX_PER_TICK = 120;

    public FluxReactorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FLUX_REACTOR_BE.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        boolean changed = false;

        if (burnTime > 0) {
            burnTime--;
            int generated = storage.receiveAetherium(FLUX_PER_TICK, false);
            if (generated > 0) changed = true;
        } else {
            ItemStack fuel = itemHandler.getStackInSlot(0);
            if (!fuel.isEmpty()) {
                int fuelValue = getFuelValue(fuel);
                if (fuelValue > 0) {
                    burnTime = fuelValue;
                    fuel.shrink(1);
                    changed = true;
                }
            }
        }

        int pushed = AetheriumNetwork.pushToNeighbors(level, pos, this, 200);
        if (pushed > 0) changed = true;

        if (changed) setChanged();
    }

    public boolean tryInsertFuel(ItemStack stack) {
        if (stack.isEmpty()) return false;
        ItemStack copy = stack.copy();
        copy.setCount(1);
        ItemStack remainder = itemHandler.insertItem(0, copy, false);
        if (remainder.isEmpty()) {
            stack.shrink(1);
            setChanged();
            return true;
        }
        return false;
    }

    private int getFuelValue(ItemStack stack) {
        String descriptionId = stack.getItem().getDescriptionId();
        if (descriptionId.contains("coal") || descriptionId.contains("charcoal")) {
            return 1600;
        }
        if (descriptionId.contains("lava")) {
            return 20000;
        }
        if (descriptionId.contains("blaze")) {
            return 2400;
        }
        return 0;
    }

    public AetheriumStorage getStorage() {
        return storage;
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public int getBurnTime() {
        return burnTime;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Aetherium", storage.getAetheriumStored());
        tag.putInt("BurnTime", burnTime);
        tag.put("Items", itemHandler.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        storage.setAetherium(tag.getInt("Aetherium"));
        burnTime = tag.getInt("BurnTime");
        if (tag.contains("Items")) {
            itemHandler.deserializeNBT(registries, tag.getCompound("Items"));
        }
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
