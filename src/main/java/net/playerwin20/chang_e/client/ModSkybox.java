package net.playerwin20.chang_e.client;

import org.jline.utils.Log;
import org.joml.Matrix3d;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector4f;
import org.slf4j.Logger;

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
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.playerwin20.chang_e.Chang_e;
import net.playerwin20.chang_e.RunTime.CEUniverse;
import net.playerwin20.chang_e.classes.Orbit;

import net.minecraft.util.Mth;

// passed orbit ahh that will supposidly be from server

@EventBusSubscriber(value = Dist.CLIENT)
public class ModSkybox {
    private static final ResourceLocation BODY =
        ResourceLocation.fromNamespaceAndPath(
            Chang_e.MODID,
            "textures/skybox/debug.png"
    );

    private static final ResourceLocation EMISSION =
        ResourceLocation.fromNamespaceAndPath(
            Chang_e.MODID,
            "textures/skybox/emission.png"
    );

    private static final float BODY_DIST_OFFSET = 100f;
    private static float FOV = 120f;
    private static long logTime = 0;

    private static final Logger LOGGER = Chang_e.LOGGER;

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

            8f*3f,
            new Vector4f(1f, 0.2f, 0.6f, 0.33f),
            EMISSION
        );

        renderPlanet(
            event.getModelViewMatrix(),
            event.getProjectionMatrix(),
            mc.gameRenderer.getMainCamera(),
            event.getPartialTick().getGameTimeDeltaPartialTick(false),

            8f,
            new Vector4f(1f, 1f, 1f, 1f),
            BODY
        );
    }

    private static void renderPlanet(
        Matrix4f modelViewMatrix,
        Matrix4f projectionMatrix,
        Camera camera,
        float partialTick,

        float fixedSize,
        Vector4f vertexColor,
        ResourceLocation texture
    ) {
        PoseStack poseStack = new PoseStack();
        poseStack.mulPose(modelViewMatrix);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(
            vertexColor.x(),
            vertexColor.y(),
            vertexColor.z(),
            vertexColor.w()
        );

        poseStack.pushPose();

        BlockPos observedPos = camera.getBlockPosition();
        double longitude = observedPos.getX();
        double latitude = observedPos.getZ();
        float mapSize = 256f; // 200187f
        longitude = (longitude / mapSize) * 360f;
        latitude = (latitude / mapSize) * 180f;

        double latRad = Math.toRadians(latitude);
        double lonRad = Math.toRadians(longitude);
        double polarX = Math.cos(latRad) * Math.cos(lonRad);
        double polarY = Math.cos(latRad) * Math.sin(lonRad);
        double polarZ = Math.sin(latRad);

        Vector3d mapNormal = new Vector3d(polarX, polarZ, polarY).normalize();
        Matrix3d polarToWorld = new Matrix3d().setLookAlong(mapNormal, new Vector3d(0, 1, 0));

        long time = Minecraft.getInstance().level.getDayTime();
        float timeSeconds = time;
        Orbit orbit = new Orbit(
            0.00257f, //AU
            0.0549f,
            5.15f,
            90f,
            0f,
            0f,

            null,
            null
        );
        Vector3d pos = orbit.getPosition(timeSeconds*0f).mul(polarToWorld);
        poseStack.translate(
            pos.x/pos.length() * BODY_DIST_OFFSET,
            pos.y/pos.length() * BODY_DIST_OFFSET,
            pos.z/pos.length() * BODY_DIST_OFFSET
        );

        long logTimeC = System.currentTimeMillis();
        if (logTimeC - logTime > 1000) {
            LOGGER.info("\n long {} \n lat {} \n normal \n     x {} \n     y {} \n     z {}",
                longitude, latitude, mapNormal.x, mapNormal.y, mapNormal.z);
            LOGGER.info("\n body distance {}", pos.length());
            logTime = logTimeC;
        }

        // direction to origin (thank you georgdroyd copilot)
        float yaw = (float) Math.toDegrees(Math.atan2(-pos.x, -pos.z));
        float pitch =
            (float) Math.toDegrees(
                Math.atan2(pos.y, Math.sqrt(pos.x * pos.x + pos.z * pos.z))
            );
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));

        Matrix4f matrix = poseStack.last().pose();
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buffer = tess.begin(
                VertexFormat.Mode.QUADS,
                DefaultVertexFormat.POSITION_TEX
        );

        float size = fixedSize; //depth

        buffer.addVertex(matrix, -size, -size, 0).setUv(0, 1);
        buffer.addVertex(matrix, size, -size, 0).setUv(1, 1);
        buffer.addVertex(matrix, size, size, 0).setUv(1, 0);
        buffer.addVertex(matrix, -size, size, 0).setUv(0, 0);

        BufferUploader.drawWithShader(buffer.buildOrThrow());

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        poseStack.popPose();
    }
}
