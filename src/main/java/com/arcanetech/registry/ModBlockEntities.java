package com.arcanetech.registry;

import com.arcanetech.ArcaneTech;
import com.arcanetech.blockentity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ArcaneTech.MOD_ID);

    public static final Supplier<BlockEntityType<AetherCapacitorBlockEntity>> AETHER_CAPACITOR_BE = 
            BLOCK_ENTITIES.register("aether_capacitor", () -> 
                    BlockEntityType.Builder.of(AetherCapacitorBlockEntity::new, ModBlocks.AETHER_CAPACITOR.get()).build(null));

    public static final Supplier<BlockEntityType<ManaWellBlockEntity>> MANA_WELL_BE = 
            BLOCK_ENTITIES.register("mana_well", () -> 
                    BlockEntityType.Builder.of(ManaWellBlockEntity::new, ModBlocks.MANA_WELL.get()).build(null));

    public static final Supplier<BlockEntityType<FluxReactorBlockEntity>> FLUX_REACTOR_BE = 
            BLOCK_ENTITIES.register("flux_reactor", () -> 
                    BlockEntityType.Builder.of(FluxReactorBlockEntity::new, ModBlocks.FLUX_REACTOR.get()).build(null));

    public static final Supplier<BlockEntityType<AetherConverterBlockEntity>> AETHER_CONVERTER_BE = 
            BLOCK_ENTITIES.register("aether_converter", () -> 
                    BlockEntityType.Builder.of(AetherConverterBlockEntity::new, ModBlocks.AETHER_CONVERTER.get()).build(null));

    public static final Supplier<BlockEntityType<AetherforgeTableBlockEntity>> AETHERFORGE_TABLE_BE = 
            BLOCK_ENTITIES.register("aetherforge_table", () -> 
                    BlockEntityType.Builder.of(AetherforgeTableBlockEntity::new, ModBlocks.AETHERFORGE_TABLE.get()).build(null));
}