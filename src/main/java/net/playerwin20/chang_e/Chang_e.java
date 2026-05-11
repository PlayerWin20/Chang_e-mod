package net.playerwin20.chang_e;
import org.joml.Math;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.playerwin20.chang_e.registry.ModBlockEntities;
import net.playerwin20.chang_e.registry.ModBlocks;
import net.playerwin20.chang_e.registry.ModItems;
import net.playerwin20.chang_e.registry.advanced.block.Regolith;
import net.playerwin20.chang_e.registry.blockentity.RegolithBlockEntity;

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
    //private static RandomSource RNG_VISUAL = RandomSource.create();
    public static int RegolithColorMap(BlockPos pos) {
        float red = Math.sin(pos.getX()) * Math.sin(pos.getY()) * Math.sin(pos.getX()) + 1;
        float green = Math.cos(pos.getY()) * Math.cos(pos.getZ()) * Math.cos(pos.getY()) * Math.cos(pos.getX()) * Math.cos(pos.getY()) + 1;
        float blue = Math.sin(pos.getZ()) * Math.cos(pos.getZ()) + 1;
        return (int) ((red+green+blue) / 3 * 0xffffff);
    }

    @EventBusSubscriber(modid = Chang_e.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientModEvents {
        @SubscribeEvent
        static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void onBlockColors(RegisterColorHandlersEvent.Block event) {
            event.register((state, level, pos, tintIndex) -> {
                BlockEntity BE = level.getBlockEntity(pos);
                if (level != null && pos != null && BE != null && BE instanceof RegolithBlockEntity) {
                    return ((RegolithBlockEntity) BE).getHexColor(); //instead of 0 get the hexcolor tag here
                }
                return 0x888888;
            }, ModBlocks.REGOLITH.get());
        }

        @SubscribeEvent
        public static void onItemColors(RegisterColorHandlersEvent.Item event) {
            event.register((stack, tintIndex) -> {
                CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
                if (data != null) {
                    CompoundTag tag = data.copyTag();
                    return tag.getInt("hex_color");
                }
                return 0x888888; // #79746d
            }, ModBlocks.REGOLITH.get().asItem());
        }
    }
}
