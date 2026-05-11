package net.playerwin20.chang_e.datagen.providers;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.playerwin20.chang_e.datagen.providers.tagproviders.*;

public class CETagProviderWrapper {
    //there are too many tag providers so idk
    
    public static void init(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        
        generator.addProvider(event.includeServer(), new CEBlockTagsProvider(
            output, lookupProvider, existingFileHelper));
    }    
}