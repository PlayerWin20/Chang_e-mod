package net.playerwin20.chang_e.registry;

import net.playerwin20.chang_e.Chang_e;
import net.playerwin20.chang_e.registry.advanced.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;;


public class ModBlocks{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Chang_e.MODID);

    //CONFIGS   does nuh

    private static final BlockBehaviour.StatePredicate REDSTONE_CONDUCT =
    new BlockBehaviour.StatePredicate() {
        @Override
        public boolean test(BlockState state, BlockGetter level, BlockPos pos) {
            return true;
        }
    };

    //INSTANCES

    public static final DeferredBlock<Block> SPEEDWALK = registerBlock("speed_walk",
        () -> new Block(BlockBehaviour.Properties.of()
        .strength(4f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.GLASS)
        .friction(1f)
    ));
    public static final DeferredBlock<Block> ACTIVESPEEDWALK = registerBlock("active_speed_walk", 
        () -> new Block(BlockBehaviour.Properties.of()
        .strength(4f)
        .requiresCorrectToolForDrops()
        .sound(SoundType.GLASS)
        .friction(0.3f)
    ));
    public static final DeferredBlock<Block> REGOLITH = registerBlock("regolith", 
        () -> new Regolith(BlockBehaviour.Properties.of()
        .strength(0.5f)
        .sound(SoundType.GRAVEL)
    ));
    public static final DeferredBlock<Block> REGOLITH_RACK = registerBlock("regolith_rack", 
        () -> new Block(BlockBehaviour.Properties.of()
        .strength(0.2f)
        .sound(SoundType.NETHERRACK)
    ));
    public static final DeferredBlock<Block> MERCURY = registerBlock("mercury", 
        () -> new Block(BlockBehaviour.Properties.of()
        .strength(5f)
        .sound(SoundType.STONE)
    ));
    public static final DeferredBlock<Block> PORTAL = registerBlock("portal", 
        () -> new NetherPortalBlock(BlockBehaviour.Properties.of()
        .strength(-1f)
        .sound(SoundType.GLASS)
        .noCollission()
    ));
    public static final DeferredBlock<Block> DEBUG_ENGINE = registerBlock("debug_engine", 
        () -> new DebugEngine(BlockBehaviour.Properties.of()
        .strength(4f)
        .sound(SoundType.METAL)
        .isRedstoneConductor(REDSTONE_CONDUCT)
    ));
    
    //REGISTRARS

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
