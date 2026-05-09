package net.playerwin20.chang_e.registry;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.playerwin20.chang_e.Chang_e;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Chang_e.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
