package net.playerwin20.chang_e.classes;

import net.minecraft.resources.ResourceLocation;

public class Cel {
    public float DEG_X, DEG_Y, DEG_Z, SIZE;
    public ResourceLocation TEXTURE;

    public Cel(float degX, float degY, float degZ, float size, ResourceLocation texture) {
        DEG_X = degX;
        DEG_Y = degY;
        DEG_Z = degZ;
        SIZE = size;
        TEXTURE = texture;
    }
}
