package net.playerwin20.chang_e.client;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.playerwin20.chang_e.Chang_e;
import net.playerwin20.chang_e.RunTime.CEUniverse;
import net.playerwin20.chang_e.classes.Cel;

// passed orbit ahh that will supposidly be from server

@EventBusSubscriber(value = Dist.CLIENT)
public class ModSkybox {
    private static final ResourceLocation BODY =
        ResourceLocation.fromNamespaceAndPath(
            Chang_e.MODID,
            "textures/skybox/debug.png"
    );

    //private static final Cel[] RENDER_PAYLOAD = CEUniverse.getRenderPayload();

    @SubscribeEvent
    public static void renderSky(RenderLevelStageEvent event) {

        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY)
            return;

        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || !CEUniverse.isBuilt())
            return;

        renderPlanet(
            event.getModelViewMatrix(),
            event.getProjectionMatrix(),
            mc.gameRenderer.getMainCamera(),
            event.getPartialTick().getGameTimeDeltaPartialTick(false),

            35f,
            10f,
            0f,
            20f,
            BODY
        );

        Cel[] RENDER_PAYLOAD = CEUniverse.getRenderPayload();
        for (int i = 0; i < RENDER_PAYLOAD.length; i++) {
            Cel payload = RENDER_PAYLOAD[i];
            renderPlanet(
                event.getModelViewMatrix(),
                event.getProjectionMatrix(),
                mc.gameRenderer.getMainCamera(),
                event.getPartialTick().getGameTimeDeltaPartialTick(false),

                payload.DEG_X,
                payload.DEG_Y,
                payload.DEG_Z,
                payload.SIZE,
                payload.TEXTURE
            );
        }
    }

    private static void renderPlanet(
        Matrix4f modelViewMatrix,
        Matrix4f projectionMatrix,
        Camera camera,
        float partialTick,

        float angX,
        float angY,
        float angZ,
        float fixedSize,
        ResourceLocation texture
    ) {
        PoseStack poseStack = new PoseStack();
        poseStack.mulPose(modelViewMatrix);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BODY);
        poseStack.pushPose();

        long time = Minecraft.getInstance().level.getDayTime();
        float angle = (time + partialTick) * 0.05f;

        poseStack.mulPose(Axis.YP.rotationDegrees(angY * angle));
        poseStack.mulPose(Axis.XP.rotationDegrees(angX * angle));
        poseStack.mulPose(Axis.ZP.rotationDegrees(angZ));

        // Push away from camera
        poseStack.translate(0, 0, -512);

        Matrix4f matrix = poseStack.last().pose();
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buffer = tess.begin(
                VertexFormat.Mode.QUADS,
                DefaultVertexFormat.POSITION_TEX
        );

        float size = fixedSize;

        buffer.addVertex(matrix, -size, -size, 0).setUv(0, 1);
        buffer.addVertex(matrix, size, -size, 0).setUv(1, 1);
        buffer.addVertex(matrix, size, size, 0).setUv(1, 0);
        buffer.addVertex(matrix, -size, size, 0).setUv(0, 0);

        BufferUploader.drawWithShader(buffer.buildOrThrow());

        poseStack.popPose();
    }
}
