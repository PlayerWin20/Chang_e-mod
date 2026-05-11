package net.playerwin20.chang_e.datagen.providers;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.playerwin20.chang_e.Chang_e;
import net.playerwin20.chang_e.registry.ModBlocks;

public class CEBlockStateProvider extends BlockStateProvider {

    public CEBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Chang_e.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.ACTIVESPEEDWALK.get(), 
            models().cubeAll("active_speed_walk", modLoc("block/speed_walk")));
        simpleBlockItem(ModBlocks.ACTIVESPEEDWALK.get(),
            models().cubeAll("active_speed_walk", modLoc("block/speed_walk")));
        
        fastSimpleBlockWithCubeAllItem(ModBlocks.MERCURY);
        fastSimpleBlockWithCubeAllItem(ModBlocks.REGOLITH);
        fastSimpleBlockWithCubeAllItem(ModBlocks.REGOLITH_RACK);
        fastSimpleBlockWithCubeAllItem(ModBlocks.SPEEDWALK);
    }

    //METHODS

    protected void fastSimpleBlockWithCubeAllItem(DeferredBlock<?> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }
}