package net.playerwin20.chang_e;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.playerwin20.chang_e.registry.ModBlockEntities;
import net.playerwin20.chang_e.registry.ModBlocks;
import net.playerwin20.chang_e.registry.ModItems;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Chang_e.MODID)
public class Chang_e {
    public static final String MODID = "chang_e"; //examplemod, change back if no work
    public static final Logger LOGGER = LogUtils.getLogger();

    //creative
    public static final DeferredRegister.Items SHALLOW_ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredItem<Item> MASCOT = SHALLOW_ITEMS.registerSimpleItem("placeholder");
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("chang_e_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.chang_e"))
        .withTabsBefore(CreativeModeTabs.COMBAT)
        .icon(() -> MASCOT.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            output.accept(MASCOT.get());
        }).build());

    public Chang_e(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup); // Register the commonSetup method for modloading
        modEventBus.addListener(this::addCreative);

        SHALLOW_ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        ModItems.register(modEventBus); // Register the Deferred Register to the mod event bus so items get registered
        ModBlocks.register(modEventBus); // Register the Deferred Register to the mod event bus so blocks get registered
        ModBlockEntities.register(modEventBus);
        //ModEffects.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == MOD_TAB.getKey()) {
            event.accept(ModBlocks.REGOLITH);
            event.accept(ModBlocks.REGOLITH_RACK);
            event.accept(ModBlocks.MERCURY);
            event.accept(ModBlocks.DEBUG_ENGINE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = Chang_e.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientModEvents {
        @SubscribeEvent
        static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
