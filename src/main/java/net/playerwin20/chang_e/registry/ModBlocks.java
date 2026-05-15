package net.playerwin20.chang_e.registry;

import net.playerwin20.chang_e.Chang_e;
import net.playerwin20.chang_e.registry.advanced.block.*;
import net.playerwin20.chang_e.registry.advanced.blockitem.*;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;;


public class ModBlocks{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Chang_e.MODID);

    private static enum blockStack {
        DEFAULT,
        SOURCE_NAMED, // changes itemstacks name to tag.getString("source_name") locally
        COMPOUND_CARRIER, // anything that drops a regolith block
        IGNITER // fent and steel
    }

    //INSTANCES

    public static final DeferredBlock<Block> SPEEDWALK = registerBlock("speed_walk",
        () -> new Block(BlockBehaviour.Properties.of()
        .strength(4f)
        .sound(SoundType.GLASS)
        .friction(1f)
        ),
        blockStack.DEFAULT
    );
    public static final DeferredBlock<Block> ACTIVESPEEDWALK = registerBlock("active_speed_walk", 
        () -> new Block(BlockBehaviour.Properties.of()
        .strength(4f)
        .sound(SoundType.GLASS)
        .friction(0.3f)
        ),
        blockStack.DEFAULT
    );

    public static final DeferredBlock<Block> REGOLITH = registerBlock("regolith", 
        () -> new Regolith(BlockBehaviour.Properties.of()
            .strength(0.5f)
            .sound(SoundType.GRAVEL)
        ),
        blockStack.SOURCE_NAMED
    );

    public static final DeferredBlock<Block> REGOLITH_RACK = registerBlock("regolith_rack", 
        () -> new Block(BlockBehaviour.Properties.of()
        .strength(0.2f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.NETHERRACK)
        ),
        blockStack.DEFAULT
    );
    public static final DeferredBlock<Block> MERCURY = registerBlock("mercury", 
        () -> new Block(BlockBehaviour.Properties.of()
        .strength(5f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.STONE)
        ),
        blockStack.DEFAULT
    );
    public static final DeferredBlock<Block> PORTAL = registerBlock("portal", 
        () -> new NetherPortalBlock(BlockBehaviour.Properties.of()
        .strength(-1f)
        .sound(SoundType.GLASS)
        .noCollission()
        ),
        blockStack.DEFAULT
    );
    public static final DeferredBlock<Block> DEBUG_ENGINE = registerBlock("debug_engine", 
        () -> new DebugEngine(BlockBehaviour.Properties.of()
        .strength(4f)
        .sound(SoundType.METAL)
        ), 
        blockStack.DEFAULT
    );
    public static final DeferredBlock<Block> SILICON_ORE = registerBlock("silicon_ore", 
        () -> new Block(BlockBehaviour.Properties.of()
        .strength(4f)
        .sound(SoundType.STONE)
        ), 
        blockStack.DEFAULT
    );

    //TODO group allat into one group
    public static final DeferredBlock<Block> CE_STONE_SURFACE = registerBlock("ce_stone_surface", 
        () -> new Block(BlockBehaviour.Properties.of()
        .strength(5f)
        .sound(SoundType.STONE)
        ), 
        blockStack.COMPOUND_CARRIER
    );

    public static final DeferredBlock<Block> CE_STONE_WALL = registerBlock("ce_stone_wall", 
        () -> new Block(BlockBehaviour.Properties.of()
        .strength(5f)
        .sound(SoundType.STONE)
        ), 
        blockStack.COMPOUND_CARRIER
    );

    //not that
    public static final DeferredBlock<Block> PLASMA = registerBlock("plasma",
        () -> new Plasma(BlockBehaviour.Properties.of()
        .noCollission()
        .sound(SoundType.ANVIL)
        ),
        blockStack.IGNITER
    );
    
    //REGISTRARS

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block, @Nullable blockStack blockItemType) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);

        if (blockItemType == null || blockItemType == blockStack.DEFAULT) {
            ModItems.ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties()));
        }
        if (blockItemType == blockStack.SOURCE_NAMED) {
            ModItems.ITEMS.register(name, () -> new RegolithBlockItem(toReturn.get(), new Item.Properties()));
        }
        if (blockItemType == blockStack.COMPOUND_CARRIER) {
            ModItems.ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties()));
        }
        if (blockItemType == blockStack.IGNITER) {
            ModItems.ITEMS.register(name, () -> new PlasmaBlockItem(toReturn.get(), new Item.Properties()));
        }

        return toReturn;
    }

    /*
    private static <T extends Block, Q extends BlockItem> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    */

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
