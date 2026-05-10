package net.playerwin20.chang_e.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.playerwin20.chang_e.Chang_e;
import net.playerwin20.chang_e.registry.effect.*;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
        DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Chang_e.MODID);

    //inst

    public static final Holder<MobEffect> CUT = MOB_EFFECTS.register("cut", 
        () -> new CutEffect(MobEffectCategory.HARMFUL, 0x111111)
    );
    public static final Holder<MobEffect> IRRADIATED = MOB_EFFECTS.register("cut", 
        () -> new IrradiatedEffect(MobEffectCategory.HARMFUL, 0xffff00)
    );
    
    //reg
    
    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
