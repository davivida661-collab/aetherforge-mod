package com.arcanetech.block;

import com.arcanetech.blockentity.AetherforgeTableBlockEntity;
import com.arcanetech.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class AetherforgeTableBlock extends BaseEntityBlock {
    public AetherforgeTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(AetherforgeTableBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AetherforgeTableBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AetherforgeTableBlockEntity table) {
            ItemStack held = player.getItemInHand(hand);
            if (!held.isEmpty()) {
                if (table.addIngredient(held)) {
                    player.displayClientMessage(Component.literal("Ingredient stored."), true);
                    return InteractionResult.CONSUME;
                }
            }

            ItemStack crafted = table.takeOutput();
            if (!crafted.isEmpty()) {
                player.getInventory().placeItemBackInInventory(crafted);
                player.displayClientMessage(Component.literal("Took " + crafted.getCount() + "x " + crafted.getHoverName().getString()), true);
                return InteractionResult.CONSUME;
            }

            player.displayClientMessage(Component.literal("Aetherforge | Power: " + table.getStorage().getAetheriumStored()), true);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.AETHERFORGE_TABLE_BE.get(),
                (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1));
    }
}
