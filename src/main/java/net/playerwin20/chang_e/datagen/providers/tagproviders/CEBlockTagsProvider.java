package net.playerwin20.chang_e.datagen.providers.tagproviders;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.playerwin20.chang_e.Chang_e;
import net.playerwin20.chang_e.registry.ModBlocks;

public class CEBlockTagsProvider extends BlockTagsProvider {

    public CEBlockTagsProvider(PackOutput output, CompletableFuture<Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Chang_e.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(ModBlocks.REGOLITH_RACK.get())
            .add(ModBlocks.MERCURY.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .add(ModBlocks.REGOLITH.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
            .add(ModBlocks.REGOLITH_RACK.get())
            .add(ModBlocks.MERCURY.get());
    }

    
}