package com.arcanetech.command;

import com.arcanetech.ArcaneTech;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ArcaneTechCommands {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("arcanetech")
            .requires(source -> source.hasPermission(0))
            .executes(ArcaneTechCommands::showHelp)
            .then(Commands.literal("help")
                .executes(ArcaneTechCommands::showHelp))
            .then(Commands.literal("info")
                .executes(ArcaneTechCommands::showInfo))
            .then(Commands.literal("energy")
                .requires(source -> source.hasPermission(2))
                .executes(ArcaneTechCommands::showEnergyInfo))
            .then(Commands.literal("give")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("core")
                    .executes(ctx -> giveAetherCore(ctx)))
                .then(Commands.literal("rune")
                    .executes(ctx -> giveRune(ctx)))));
    }

    private static int showHelp(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        source.sendSuccess(() -> Component.literal("=== ArcaneTech Commands ==="), false);
        source.sendSuccess(() -> Component.literal("/arcanetech help - Show this help"), false);
        source.sendSuccess(() -> Component.literal("/arcanetech info - Show mod info"), false);
        source.sendSuccess(() -> Component.literal("/arcanetech energy - Show energy info (admin)"), false);
        source.sendSuccess(() -> Component.literal("/arcanetech give core - Give Aether Core (admin)"), false);
        source.sendSuccess(() -> Component.literal("/arcanetech give rune - Give Elemental Rune (admin)"), false);
        return 1;
    }

    private static int showInfo(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        source.sendSuccess(() -> Component.literal("=== ArcaneTech - Aetherforge ==="), false);
        source.sendSuccess(() -> Component.literal("A mod combining magic and technology!"), false);
        source.sendSuccess(() -> Component.literal("Create Aetherium from Mana and Flux!"), false);
        source.sendSuccess(() -> Component.literal("Build machines, craft runes, explore the Aether!"), false);
        return 1;
    }

    private static int showEnergyInfo(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        source.sendSuccess(() -> Component.literal("=== Aetherium Energy System ==="), false);
        source.sendSuccess(() -> Component.literal("Mana Well: Slow, natural generation"), false);
        source.sendSuccess(() -> Component.literal("Flux Reactor: Fast generation with fuel"), false);
        source.sendSuccess(() -> Component.literal("Aether Converter: Converts between types"), false);
        source.sendSuccess(() -> Component.literal("Aether Capacitor: Stores up to 100,000 Aetherium"), false);
        return 1;
    }

    private static int giveAetherCore(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (source.getEntity() instanceof ServerPlayer player) {
            player.getInventory().add(new net.minecraft.world.item.ItemStack(
                net.minecraft.core.registries.BuiltInRegistries.ITEM.get(
                    net.minecraft.resources.ResourceLocation.tryParse(ArcaneTech.MOD_ID + ":aether_core")
                )
            ));
            source.sendSuccess(() -> Component.literal("Gave Aether Core to " + player.getName().getString()), false);
        }
        return 1;
    }

    private static int giveRune(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (source.getEntity() instanceof ServerPlayer player) {
            player.getInventory().add(new net.minecraft.world.item.ItemStack(
                net.minecraft.core.registries.BuiltInRegistries.ITEM.get(
                    net.minecraft.resources.ResourceLocation.tryParse(ArcaneTech.MOD_ID + ":elemental_rune_fire")
                )
            ));
            source.sendSuccess(() -> Component.literal("Gave Fire Rune to " + player.getName().getString()), false);
        }
        return 1;
    }
}