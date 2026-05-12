package net.playerwin20.chang_e.registry;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.playerwin20.chang_e.Chang_e;
import net.playerwin20.chang_e.registry.advanced.blockitem.*;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Chang_e.MODID);

    public static final DeferredItem<Item> DEBUG = ITEMS.register("debug", 
        () -> new Item(new Item.Properties())
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
