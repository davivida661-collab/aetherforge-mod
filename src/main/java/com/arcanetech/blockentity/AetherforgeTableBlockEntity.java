package com.arcanetech.blockentity;

import com.arcanetech.energy.AetheriumNetwork;
import com.arcanetech.energy.AetheriumStorage;
import com.arcanetech.energy.IAetheriumHandler;
import com.arcanetech.registry.ModBlockEntities;
import com.arcanetech.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class AetherforgeTableBlockEntity extends BlockEntity implements IAetheriumHandler {
    private final AetheriumStorage storage = new AetheriumStorage(5000, 200, 0);
    private final ItemStackHandler itemHandler = new ItemStackHandler(9);
    private int craftingProgress = 0;
    private static final int CRAFTING_TIME = 100;

    public AetherforgeTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AETHERFORGE_TABLE_BE.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        boolean changed = false;

        int pulled = AetheriumNetwork.pullFromNeighbors(level, pos, this, 120);
        if (pulled > 0) changed = true;

        if (hasValidRecipe()) {
            if (storage.getAetheriumStored() >= 10) {
                craftingProgress++;
                if (craftingProgress >= CRAFTING_TIME) {
                    craftingProgress = 0;
                    storage.extractAetherium(10, false);
                    processCrafting();
                    changed = true;
                }
            }
        } else {
            if (craftingProgress != 0) changed = true;
            craftingProgress = 0;
        }

        if (changed) setChanged();
    }

    private boolean hasValidRecipe() {
        ItemStack result = resolveResult();
        if (result.isEmpty()) return false;

        ItemStack outputSlot = itemHandler.getStackInSlot(8);
        if (outputSlot.isEmpty()) return true;
        if (!ItemStack.isSameItemSameComponents(outputSlot, result)) return false;
        return outputSlot.getCount() + result.getCount() <= outputSlot.getMaxStackSize();
    }

    private ItemStack resolveResult() {
        ItemStack input1 = itemHandler.getStackInSlot(0);
        ItemStack input2 = itemHandler.getStackInSlot(1);

        if (input1.isEmpty() || input2.isEmpty()) return ItemStack.EMPTY;

        if (isRune(input1) && input2.is(ModItems.CIRCUIT_PLATE.get()) ||
            isRune(input2) && input1.is(ModItems.CIRCUIT_PLATE.get())) {
            return new ItemStack(ModItems.AETHER_CORE.get());
        }

        if ((input1.is(ModItems.AETHER_CORE.get()) && input2.is(ModItems.CIRCUIT_PLATE.get())) ||
                (input2.is(ModItems.AETHER_CORE.get()) && input1.is(ModItems.CIRCUIT_PLATE.get()))) {
            return new ItemStack(ModItems.AETHERIUM_CRYSTAL.get());
        }

        if ((isRune(input1) && input2.is(ModItems.AETHERIUM_CRYSTAL.get())) ||
                (isRune(input2) && input1.is(ModItems.AETHERIUM_CRYSTAL.get()))) {
            return new ItemStack(ModItems.AETHERITE_PICKAXE.get());
        }

        return ItemStack.EMPTY;
    }

    private boolean isRune(ItemStack stack) {
        return stack.is(ModItems.ELEMENTAL_RUNE_AIR.get())
                || stack.is(ModItems.ELEMENTAL_RUNE_WATER.get())
                || stack.is(ModItems.ELEMENTAL_RUNE_EARTH.get())
                || stack.is(ModItems.ELEMENTAL_RUNE_FIRE.get());
    }

    private void processCrafting() {
        ItemStack result = resolveResult();
        if (result.isEmpty()) return;

        itemHandler.getStackInSlot(0).shrink(1);
        itemHandler.getStackInSlot(1).shrink(1);

        ItemStack outputSlot = itemHandler.getStackInSlot(8);
        if (outputSlot.isEmpty()) {
            itemHandler.setStackInSlot(8, result);
        } else if (ItemStack.isSameItemSameComponents(outputSlot, result)) {
            outputSlot.grow(result.getCount());
        }
    }

    public boolean addIngredient(ItemStack stack) {
        if (stack.isEmpty()) return false;
        for (int i = 0; i < 2; i++) {
            ItemStack slotStack = itemHandler.getStackInSlot(i);
            if (slotStack.isEmpty()) {
                ItemStack copy = stack.copy();
                copy.setCount(1);
                itemHandler.setStackInSlot(i, copy);
                stack.shrink(1);
                setChanged();
                return true;
            } else if (ItemStack.isSameItemSameComponents(slotStack, stack) && slotStack.getCount() < slotStack.getMaxStackSize()) {
                slotStack.grow(1);
                stack.shrink(1);
                setChanged();
                return true;
            }
        }
        return false;
    }

    public ItemStack takeOutput() {
        ItemStack output = itemHandler.getStackInSlot(8);
        if (output.isEmpty()) return ItemStack.EMPTY;
        ItemStack extracted = output.copy();
        itemHandler.setStackInSlot(8, ItemStack.EMPTY);
        setChanged();
        return extracted;
    }

    public AetheriumStorage getStorage() {
        return storage;
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public int getCraftingProgress() {
        return craftingProgress;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Aetherium", storage.getAetheriumStored());
        tag.putInt("CraftingProgress", craftingProgress);
        tag.put("Items", itemHandler.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        storage.setAetherium(tag.getInt("Aetherium"));
        craftingProgress = tag.getInt("CraftingProgress");
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
