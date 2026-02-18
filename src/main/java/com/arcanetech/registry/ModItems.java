package com.arcanetech.registry;

import com.arcanetech.ArcaneTech;
import com.arcanetech.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ArcaneTech.MOD_ID);

    public static final DeferredItem<Item> AETHER_ORE_BLOCK = registerBlockItem("aether_ore", ModBlocks.AETHER_ORE);
    public static final DeferredItem<Item> DEEPSLATE_AETHER_ORE_BLOCK = registerBlockItem("deepslate_aether_ore", ModBlocks.DEEPSLATE_AETHER_ORE);
    public static final DeferredItem<Item> AETHERIUM_BLOCK_ITEM = registerBlockItem("aetherium_block", ModBlocks.AETHERIUM_BLOCK);
    public static final DeferredItem<Item> AETHER_CAPACITOR_BLOCK = registerBlockItem("aether_capacitor", ModBlocks.AETHER_CAPACITOR);
    public static final DeferredItem<Item> MANA_WELL_BLOCK = registerBlockItem("mana_well", ModBlocks.MANA_WELL);
    public static final DeferredItem<Item> FLUX_REACTOR_BLOCK = registerBlockItem("flux_reactor", ModBlocks.FLUX_REACTOR);
    public static final DeferredItem<Item> AETHER_CONVERTER_BLOCK = registerBlockItem("aether_converter", ModBlocks.AETHER_CONVERTER);
    public static final DeferredItem<Item> AETHERFORGE_TABLE_BLOCK = registerBlockItem("aetherforge_table", ModBlocks.AETHERFORGE_TABLE);

    public static final DeferredItem<Item> AETHER_CORE = ITEMS.register("aether_core",
            () -> new AetherCoreItem(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> RAW_AETHERITE = ITEMS.register("raw_aetherite",
            () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON)));

    public static final DeferredItem<Item> AETHERITE_INGOT = ITEMS.register("aetherite_ingot",
            () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> AETHERIUM_CRYSTAL = ITEMS.register("aetherium_crystal",
            () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.RARE)));

    public static final DeferredItem<Item> MANA_DUST = ITEMS.register("mana_dust",
            () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON)));

    public static final DeferredItem<Item> FLUX_DUST = ITEMS.register("flux_dust",
            () -> new Item(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON)));

    public static final DeferredItem<Item> CIRCUIT_PLATE = ITEMS.register("circuit_plate",
            () -> new CircuitPlateItem(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> ELEMENTAL_RUNE_FIRE = ITEMS.register("elemental_rune_fire",
            () -> new ElementalRuneItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));

    public static final DeferredItem<Item> ELEMENTAL_RUNE_WATER = ITEMS.register("elemental_rune_water",
            () -> new ElementalRuneItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));

    public static final DeferredItem<Item> ELEMENTAL_RUNE_EARTH = ITEMS.register("elemental_rune_earth",
            () -> new ElementalRuneItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));

    public static final DeferredItem<Item> ELEMENTAL_RUNE_AIR = ITEMS.register("elemental_rune_air",
            () -> new ElementalRuneItem(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));

    public static final DeferredItem<Item> AETHERITE_PICKAXE = ITEMS.register("aetherite_pickaxe",
            () -> new TeleportPickaxeItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).durability(2000)));

    public static final DeferredItem<Item> AETHERITE_SWORD = ITEMS.register("aetherite_sword",
            () -> new SwordItem(Tiers.DIAMOND, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).durability(2000)));

    public static final DeferredItem<Item> AETHERITE_HELMET = ITEMS.register("aetherite_helmet",
            () -> new JetpackArmorItem(ArmorItem.Type.HELMET,
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> AETHERITE_CHESTPLATE = ITEMS.register("aetherite_chestplate",
            () -> new JetpackArmorItem(ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> AETHERITE_LEGGINGS = ITEMS.register("aetherite_leggings",
            () -> new JetpackArmorItem(ArmorItem.Type.LEGGINGS,
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> AETHERITE_BOOTS = ITEMS.register("aetherite_boots",
            () -> new JetpackArmorItem(ArmorItem.Type.BOOTS,
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    private static DeferredItem<Item> registerBlockItem(String name, DeferredBlock<? extends Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
