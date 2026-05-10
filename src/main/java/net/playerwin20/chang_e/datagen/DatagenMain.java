package net.playerwin20.chang_e.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.playerwin20.chang_e.Chang_e;
import net.playerwin20.chang_e.datagen.providers.*;

@EventBusSubscriber(modid = Chang_e.MODID)
public class DatagenMain {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new CEBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new CEItemModelProvider(output, existingFileHelper));

        generator.addProvider(event.includeServer(), new CERecipeProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new CELootTableProvider(output, lookupProvider));

        CETagProviderWrapper.init(event);
    }
}