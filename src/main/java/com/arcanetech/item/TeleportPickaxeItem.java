package com.arcanetech.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TeleportPickaxeItem extends PickaxeItem {
    public TeleportPickaxeItem(Properties properties) {
        super(Tiers.DIAMOND, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (!level.isClientSide && player != null) {
            BlockState state = level.getBlockState(pos);
            if (state.getDestroySpeed(level, pos) >= 0) {
                if (stack.getDamageValue() < stack.getMaxDamage() - 10) {
                    stack.hurtAndBreak(10, player, EquipmentSlot.MAINHAND);
                    
                    BlockPos targetPos = pos.offset(context.getClickedFace().getNormal());
                    player.teleportTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
                    
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.PORTAL, 
                            player.getX(), player.getY() + 1, player.getZ(), 
                            50, 0.5, 0.5, 0.5, 0.1);
                    }
                    
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.useOn(context);
    }
}