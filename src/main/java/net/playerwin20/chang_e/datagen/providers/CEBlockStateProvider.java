package net.playerwin20.chang_e.datagen.providers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
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
        fastSimpleBlockWithCubeAllItem(ModBlocks.REGOLITH_RACK);
        fastSimpleBlockWithCubeAllItem(ModBlocks.SPEEDWALK);

        fastSimpleBlockWithCubeAllItem(ModBlocks.CE_STONE_SURFACE);
        fastSimpleBlockWithCubeAllItem(ModBlocks.CE_STONE_WALL);

        fastSimpleBlockWithCubeAllItem(ModBlocks.SILICON_ORE);

        fastSimpleBlockWithTintCubeAllItem(ModBlocks.REGOLITH);
    }

    //DISTINCT MODEL FILES
    protected ModelFile tintedCubeAll(Block block) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();

        return models().getBuilder(name)
            .parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
            .texture("particle", blockTexture(block))
            .texture("down", blockTexture(block))
            .texture("up", blockTexture(block))
            .texture("north", blockTexture(block))
            .texture("south", blockTexture(block))
            .texture("west", blockTexture(block))
            .texture("east", blockTexture(block))
            .element()
                .from(0, 0, 0)
                .to(16, 16, 16)

                .allFaces((dir, face) -> face
                    .uvs(0, 0, 16, 16)
                    .texture("#" + dir.getName())
                    .tintindex(0)
                )
        .end();
    }

    //METHODS

    protected void fastSimpleBlockWithCubeAllItem(DeferredBlock<?> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    protected void fastSimpleBlockWithTintCubeAllItem(DeferredBlock<?> block) {
        simpleBlockWithItem(block.get(), tintedCubeAll(block.get()));
    }
}