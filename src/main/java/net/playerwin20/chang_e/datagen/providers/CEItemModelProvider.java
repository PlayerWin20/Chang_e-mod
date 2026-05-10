package net.playerwin20.chang_e.datagen.providers;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.playerwin20.chang_e.Chang_e;
import net.playerwin20.chang_e.registry.ModItems;

public class CEItemModelProvider extends ItemModelProvider{

    public CEItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Chang_e.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.DEBUG.get());
    }

}