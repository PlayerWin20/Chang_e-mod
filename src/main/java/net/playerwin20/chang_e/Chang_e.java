package net.playerwin20.chang_e;

import net.playerwin20.chang_e.RunTime.CEUniverse;
import net.playerwin20.chang_e.registry.ModBlockEntities;
import net.playerwin20.chang_e.registry.ModBlocks;
import net.playerwin20.chang_e.registry.ModItems;
import net.playerwin20.chang_e.registry.blockentity.*;

import java.util.List;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.client.Minecraft;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

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

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
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
            event.accept(ModBlocks.CE_STONE_SURFACE);
            event.accept(ModBlocks.CE_STONE_WALL);
            event.accept(ModBlocks.MERCURY);
            event.accept(ModBlocks.DEBUG_ENGINE);

            event.accept(ModBlocks.PLASMA.asItem());
            event.accept(ModBlocks.SILICON_ORE);
            event.accept(ModItems.SILICON_INGOT);
            event.accept(ModItems.RAW_SILICON);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        //CEUniverse.initialize(event); // sole purpose of it being here is so objects can be added at runtime (but idk about dimensions)
    }

    private static WorldgenRandom debug_RNG = new WorldgenRandom(new LegacyRandomSource(0));
    private static PerlinNoise debug_RED = PerlinNoise.create(debug_RNG, List.of(5));
    private static PerlinNoise debug_GRN = PerlinNoise.create(debug_RNG, List.of(11));
    private static PerlinNoise debug_BLU = PerlinNoise.create(debug_RNG, List.of(17));

    public static int DebugColorMap(BlockPos pos) {
        double r = debug_RED.getValue(pos.getX(), pos.getY(), pos.getZ());
        double g = debug_GRN.getValue(pos.getX(), pos.getY(), pos.getZ());
        double b = debug_BLU.getValue(pos.getX(), pos.getY(), pos.getZ());
        double noise = (r+1)/2*(g+1)/2*(b+1)/2;

        return (int) (noise * 0xffffff);
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
                if (level != null && pos != null) {
                    BlockEntity BE = level.getBlockEntity(pos);
                    if (BE != null && BE instanceof RegolithBlockEntity) {
                        return ((RegolithBlockEntity) BE).getHexColor(); //instead of 0 get the hexcolor tag here
                    }
                    else {
                        return DebugColorMap(pos);
                    }
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
