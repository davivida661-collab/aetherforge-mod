package com.arcanetech;

import com.arcanetech.command.ArcaneTechCommands;
import com.arcanetech.registry.ModBlockEntities;
import com.arcanetech.registry.ModBlocks;
import com.arcanetech.registry.ModItems;
import com.arcanetech.item.JetpackArmorItem;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

@Mod(value = ArcaneTech.MOD_ID, dist = Dist.DEDICATED_SERVER)
public class ArcaneTech {
    public static final String MOD_ID = "arcanetech";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ArcaneTech(IEventBus modEventBus) {
        LOGGER.info("Initializing ArcaneTech - Aetherforge: A Revolucao Magitech");

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("ArcaneTech server-side systems initialized!");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ArcaneTechCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestItem.getItem() instanceof JetpackArmorItem jetpack) {
            jetpack.tickJetpack(player, chestItem);
        }
    }
}
