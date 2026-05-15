package net.playerwin20.chang_e.registry;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.playerwin20.chang_e.Chang_e;
import net.playerwin20.chang_e.registry.advanced.blockitem.*;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Chang_e.MODID);

    // ITEMS

    public static final DeferredItem<Item> DEBUG = ITEMS.register("debug", 
        () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> RAW_SILICON = ITEMS.register("raw_silicon", 
        () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> SILICON_INGOT = ITEMS.register("silicon_ingot", 
        () -> new Item(new Item.Properties())
    );

    // REGISTER

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
