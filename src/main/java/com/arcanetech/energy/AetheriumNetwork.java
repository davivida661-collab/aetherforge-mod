package com.arcanetech.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Server-side helper for moving Aetherium between neighboring machines.
 */
public final class AetheriumNetwork {

    private AetheriumNetwork() {
    }

    /**
     * Pushes energy outwards to adjacent {@link IAetheriumHandler}s.
     *
     * @return total amount moved
     */
    public static int pushToNeighbors(Level level, BlockPos pos, IAetheriumHandler source, int perNeighbor) {
        if (level == null || level.isClientSide || !source.canExtract() || perNeighbor <= 0) {
            return 0;
        }

        int moved = 0;
        for (Direction direction : Direction.values()) {
            if (source.getAetheriumStored() <= 0) break;

            BlockEntity targetEntity = level.getBlockEntity(pos.relative(direction));
            if (targetEntity instanceof IAetheriumHandler target && target.canReceive()) {
                int offered = Math.min(perNeighbor, source.getAetheriumStored());
                int accepted = target.receiveAetherium(offered, false);
                if (accepted > 0) {
                    source.extractAetherium(accepted, false);
                    moved += accepted;
                }
            }
        }

        return moved;
    }

    /**
     * Pulls energy from adjacent {@link IAetheriumHandler}s into the receiver.
     *
     * @return total amount moved
     */
    public static int pullFromNeighbors(Level level, BlockPos pos, IAetheriumHandler receiver, int perNeighbor) {
        if (level == null || level.isClientSide || !receiver.canReceive() || perNeighbor <= 0) {
            return 0;
        }

        int moved = 0;
        int space = receiver.getMaxAetheriumStored() - receiver.getAetheriumStored();
        if (space <= 0) return 0;

        for (Direction direction : Direction.values()) {
            if (space <= 0) break;

            BlockEntity neighbor = level.getBlockEntity(pos.relative(direction));
            if (neighbor instanceof IAetheriumHandler supplier && supplier.canExtract()) {
                int potential = Math.min(perNeighbor, Math.min(space, supplier.getAetheriumStored()));
                if (potential <= 0) continue;

                int accepted = receiver.receiveAetherium(potential, true);
                if (accepted <= 0) continue;

                int extracted = supplier.extractAetherium(accepted, false);
                if (extracted > 0) {
                    int stored = receiver.receiveAetherium(extracted, false);
                    moved += stored;
                    space -= stored;
                }
            }
        }

        return moved;
    }
}
