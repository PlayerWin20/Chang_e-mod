package net.playerwin20.chang_e.registry;

import net.playerwin20.chang_e.Chang_e;
import net.playerwin20.chang_e.registry.blockentity.RegolithBlockEntity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
    DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Chang_e.MODID);

    // inst

    public static final Supplier<BlockEntityType<RegolithBlockEntity>> REGOLITH_BE =
    BLOCK_ENTITIES.register("regolith_be", () -> BlockEntityType.Builder.of(
        RegolithBlockEntity::new, ModBlocks.REGOLITH.get()).build(null));

    public static final Supplier<BlockEntityType<RegolithBlockEntity>> DEBUG_ENGINE_BE =
    BLOCK_ENTITIES.register("debug_engine_be", () -> BlockEntityType.Builder.of(
        RegolithBlockEntity::new, ModBlocks.DEBUG_ENGINE.get()).build(null));
    
    // reg
    
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
