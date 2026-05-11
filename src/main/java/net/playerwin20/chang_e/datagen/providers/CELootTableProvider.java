package net.playerwin20.chang_e.datagen.providers;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.playerwin20.chang_e.datagen.providers.loottablesubproviders.*;

public class CELootTableProvider extends LootTableProvider {

    public CELootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Set.of(), List.of(
            new SubProviderEntry(
                CEBlockLootSubProvider::new, 
                LootContextParamSets.BLOCK
            )
        ), lookupProvider);
    }

}