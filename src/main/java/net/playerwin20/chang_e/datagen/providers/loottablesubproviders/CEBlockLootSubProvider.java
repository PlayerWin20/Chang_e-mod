package net.playerwin20.chang_e.datagen.providers.loottablesubproviders;

import java.util.Set;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.playerwin20.chang_e.registry.ModBlocks;
import net.playerwin20.chang_e.registry.ModItems;

public class CEBlockLootSubProvider extends BlockLootSubProvider {

    public CEBlockLootSubProvider(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries()
                .stream()
                .map(e -> (Block) e.value())
                .toList();
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.MERCURY.get());
        dropSelf(ModBlocks.REGOLITH_RACK.get());
        dropSelf(ModBlocks.REGOLITH.get());

        add(ModBlocks.DEBUG_ENGINE.get(), noDrop());
        add(ModBlocks.SPEEDWALK.get(), noDrop());
        add(ModBlocks.ACTIVESPEEDWALK.get(), noDrop());
        add(ModBlocks.PORTAL.get(), noDrop());
        add(ModBlocks.CE_STONE_SURFACE.get(), noDrop());
        add(ModBlocks.CE_STONE_WALL.get(), noDrop());

        dropOther(ModBlocks.SILICON_ORE.get(), ModItems.RAW_SILICON.get());
    }
}