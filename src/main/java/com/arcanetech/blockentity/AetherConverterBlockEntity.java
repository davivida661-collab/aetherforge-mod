package com.arcanetech.blockentity;

import com.arcanetech.energy.AetheriumNetwork;
import com.arcanetech.energy.AetheriumStorage;
import com.arcanetech.energy.IAetheriumHandler;
import com.arcanetech.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AetherConverterBlockEntity extends BlockEntity implements IAetheriumHandler {
    private final AetheriumStorage storage = new AetheriumStorage(10000, 500, 500);
    private int conversionMode = MODE_MANA_TO_FLUX;
    private int conversionProgress = 0;
    private static final int CONVERSION_TIME = 40;
    private static final int CONVERSION_AMOUNT = 120;

    public static final int MODE_MANA_TO_FLUX = 0;
    public static final int MODE_FLUX_TO_MANA = 1;

    public AetherConverterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AETHER_CONVERTER_BE.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        boolean changed = false;

        if (conversionMode == MODE_MANA_TO_FLUX) {
            changed |= pullPreferred(level, pos, ManaWellBlockEntity.class, 200) > 0;
        } else {
            changed |= pullPreferred(level, pos, FluxReactorBlockEntity.class, 200) > 0;
        }

        int pulled = AetheriumNetwork.pullFromNeighbors(level, pos, this, 120);
        if (pulled > 0) changed = true;

        conversionProgress++;
        if (conversionProgress >= CONVERSION_TIME && storage.getAetheriumStored() > 0) {
            conversionProgress = 0;
            int extracted = storage.extractAetherium(Math.min(CONVERSION_AMOUNT, storage.getAetheriumStored()), false);
            if (extracted > 0) {
                float efficiency = conversionMode == MODE_MANA_TO_FLUX ? 1.0F : 0.9F;
                int converted = Math.max(1, Math.round(extracted * efficiency));
                storage.receiveAetherium(converted, false);
                changed = true;
            }
        }

        int pushRate = conversionMode == MODE_MANA_TO_FLUX ? 260 : 140;
        int pushed = AetheriumNetwork.pushToNeighbors(level, pos, this, pushRate);
        if (pushed > 0) changed = true;

        if (changed) setChanged();
    }

    private int pullPreferred(Level level, BlockPos pos, Class<? extends BlockEntity> preferred, int perNeighbor) {
        int moved = 0;
        for (Direction direction : Direction.values()) {
            if (storage.getAetheriumStored() >= storage.getMaxAetheriumStored()) break;
            BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));
            if (preferred.isInstance(neighbor) && neighbor instanceof IAetheriumHandler handler && handler.canExtract()) {
                int space = storage.getMaxAetheriumStored() - storage.getAetheriumStored();
                int potential = Math.min(perNeighbor, Math.min(space, handler.getAetheriumStored()));
                if (potential <= 0) continue;
                int accepted = storage.receiveAetherium(potential, true);
                if (accepted <= 0) continue;
                int extracted = handler.extractAetherium(accepted, false);
                if (extracted > 0) {
                    storage.receiveAetherium(extracted, false);
                    moved += extracted;
                }
            }
        }
        return moved;
    }

    public AetheriumStorage getStorage() {
        return storage;
    }

    public int getConversionMode() {
        return conversionMode;
    }

    public String getConversionLabel() {
        return conversionMode == MODE_MANA_TO_FLUX ? "Mana -> Flux" : "Flux -> Mana";
    }

    public void setConversionMode(int mode) {
        this.conversionMode = mode;
        setChanged();
    }

    public void cycleMode() {
        conversionMode = (conversionMode + 1) % 2;
        conversionProgress = 0;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Aetherium", storage.getAetheriumStored());
        tag.putInt("ConversionMode", conversionMode);
        tag.putInt("ConversionProgress", conversionProgress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        storage.setAetherium(tag.getInt("Aetherium"));
        conversionMode = tag.getInt("ConversionMode");
        conversionProgress = tag.getInt("ConversionProgress");
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
