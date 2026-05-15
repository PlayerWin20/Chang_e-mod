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

        if (mc.level == null)
            return;

        renderPlanet1(
            event.getModelViewMatrix(),
            event.getProjectionMatrix(),
            mc.gameRenderer.getMainCamera(),
            event.getPartialTick().getGameTimeDeltaPartialTick(false)
        );
    }

    private static void renderPlanet1(
        Matrix4f modelViewMatrix,
        Matrix4f projectionMatrix,
        Camera camera,
        float partialTick
    ) {
        PoseStack poseStack = new PoseStack();

        poseStack.mulPose(modelViewMatrix);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BODY);

        poseStack.pushPose();

        // Orbit rotation
        long time = Minecraft.getInstance().level.getDayTime();

        float angle = (time + partialTick) * 0.05f;

        poseStack.mulPose(Axis.YP.rotationDegrees(angle));

        // Sky elevation
        poseStack.mulPose(Axis.XP.rotationDegrees(35f));

        // Push away from camera
        poseStack.translate(0, 0, -100);

        Matrix4f matrix = poseStack.last().pose();

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buffer = tess.begin(
                VertexFormat.Mode.QUADS,
                DefaultVertexFormat.POSITION_TEX
        );

        float size = 25f;

        buffer.addVertex(matrix, -size, -size, 0).setUv(0, 1);
        buffer.addVertex(matrix, size, -size, 0).setUv(1, 1);
        buffer.addVertex(matrix, size, size, 0).setUv(1, 0);
        buffer.addVertex(matrix, -size, size, 0).setUv(0, 0);

        BufferUploader.drawWithShader(buffer.buildOrThrow());

        poseStack.popPose();
    }
}
