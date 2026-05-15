package net.playerwin20.chang_e.classes;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import com.mojang.math.Axis;

public class Cel {
    private static float DEG_X, DEG_Y, DEG_Z, SIZE;
    private static ResourceLocation TEXTURE;

    public Cel(float degX, float degY, float degZ, float size, ResourceLocation texture) {
        DEG_X = degX;
        DEG_Y = degY;
        DEG_Z = degZ;
        SIZE = size;
        TEXTURE = texture;
    }

    public static void render(
        Matrix4f modelViewMatrix,
        Matrix4f projectionMatrix,
        Camera camera,
        float partialTick
    ) {
        PoseStack poseStack = new PoseStack();
        poseStack.mulPose(modelViewMatrix);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);

        poseStack.pushPose();

        // Orbit rotation
        long time = Minecraft.getInstance().level.getDayTime();

        float angle = (time + partialTick) * 0.5f;

        poseStack.mulPose(Axis.XP.rotationDegrees(DEG_X * angle));
        poseStack.mulPose(Axis.YP.rotationDegrees(DEG_Y * angle));
        poseStack.mulPose(Axis.ZP.rotationDegrees(DEG_Z * angle));

        poseStack.translate(0, 0, -100);

        
        BufferBuilder buffer = Tesselator.getInstance().begin(
                VertexFormat.Mode.QUADS,
                DefaultVertexFormat.POSITION_TEX
        );

        float size = SIZE;

        Matrix4f matrix = poseStack.last().pose();
        buffer.addVertex(matrix, -size, -size, 0).setUv(0, 1);
        buffer.addVertex(matrix, size, -size, 0).setUv(1, 1);
        buffer.addVertex(matrix, size, size, 0).setUv(1, 0);
        buffer.addVertex(matrix, -size, size, 0).setUv(0, 0);

        BufferUploader.drawWithShader(buffer.buildOrThrow());

        poseStack.popPose();
    }
}
