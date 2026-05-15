package net.playerwin20.chang_e.RunTime;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.playerwin20.chang_e.Chang_e;
import net.playerwin20.chang_e.classes.Cel;

public class CEUniverse {
    private static Cel[] RenderBus;

    private static RandomSource RNG;

    private static final ResourceLocation BODY =
    ResourceLocation.fromNamespaceAndPath(
        Chang_e.MODID,
        "textures/skybox/debug.png"
    );

    private static boolean built = false;
    public static void initialize(ServerStartingEvent event) {
        if (!built) {
            /*
            for(int rand = 0; rand < RNG.nextInt(1, 8); rand++) {
                RenderBus[rand] = new Cel(
                    (float) RNG.nextDouble()*2-1,
                    (float) RNG.nextDouble()*2-1,
                    (float) RNG.nextDouble()*2-1,
                    (float) RNG.nextDouble()*16,
                    BODY
                );
            }
            */
            built = true;
        } 
        else {System.err.println("Chang'e StarMap initiated twice.");} //doesnt do jack ahh
    }

    public static boolean isBuilt() {return built;}
    public static Cel[] getRenderPayload() {return RenderBus;}
}
