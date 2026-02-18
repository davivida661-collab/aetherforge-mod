package com.arcanetech.registry;

import com.arcanetech.ArcaneTech;
import com.arcanetech.block.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ArcaneTech.MOD_ID);

    public static final DeferredBlock<Block> AETHER_ORE = registerBlock("aether_ore",
            () -> new AetherOreBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> DEEPSLATE_AETHER_ORE = registerBlock("deepslate_aether_ore",
            () -> new AetherOreBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .strength(4.5F, 3.0F)
                    .sound(SoundType.DEEPSLATE)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> AETHERIUM_BLOCK = registerBlock("aetherium_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> AETHER_CAPACITOR = registerBlock("aether_capacitor",
            () -> new AetherCapacitorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5F, 3.5F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MANA_WELL = registerBlock("mana_well",
            () -> new ManaWellBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.EMERALD)
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.AMETHYST)
                    .lightLevel(state -> 7)));

    public static final DeferredBlock<Block> FLUX_REACTOR = registerBlock("flux_reactor",
            () -> new FluxReactorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NETHER)
                    .strength(4.0F, 4.0F)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> AETHER_CONVERTER = registerBlock("aether_converter",
            () -> new AetherConverterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.LAPIS)
                    .strength(3.5F, 3.5F)
                    .sound(SoundType.COPPER)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> AETHERFORGE_TABLE = registerBlock("aetherforge_table",
            () -> new AetherforgeTableBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .strength(2.5F, 2.5F)
                    .sound(SoundType.ANVIL)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        return toReturn;
    }
}