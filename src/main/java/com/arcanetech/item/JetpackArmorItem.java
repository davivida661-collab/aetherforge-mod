package com.arcanetech.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class JetpackArmorItem extends ArmorItem {
    private static final UUID JETPACK_UUID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
    private static final int FLIGHT_COST = 1;

    public JetpackArmorItem(Type type, Properties properties) {
        super(ArmorMaterials.DIAMOND, type, properties);
    }

    public void tickJetpack(Player player, ItemStack stack) {
        if (getType() != Type.CHESTPLATE) return;
        if (player.isCreative() || player.isSpectator()) return;

        Level level = player.level();
        boolean boosting = player.isSprinting() && player.getDeltaMovement().y > -0.15;
        if (!boosting) return;

        if (stack.getDamageValue() >= stack.getMaxDamage() - FLIGHT_COST) return;

        player.setDeltaMovement(player.getDeltaMovement().add(0, 0.08, 0));
        player.fallDistance = 0;
        stack.hurtAndBreak(FLIGHT_COST, player, EquipmentSlot.CHEST);

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLAME,
                    player.getX(), player.getY() + 0.5, player.getZ(),
                    6, 0.2, 0.1, 0.2, 0.01);
        }
    }
}
