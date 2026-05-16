package net.playerwin20.chang_e.classes;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class Orbit {
    float ECC;
    public Vector3f LOC_PE, LOC_AP;
    public Cel BODY;

    public Orbit(@Nullable Cel parentBody, Vector3f localApoapsis, float eccentricity) {
        float xAp = localApoapsis.x;
        float yAp = localApoapsis.y;
        float zAp = localApoapsis.z;

        float lAp = localApoapsis.length();
        float rPe = -(lAp*eccentricity-lAp) / (1 + eccentricity);

        this.ECC = eccentricity;
        this.LOC_PE = new Vector3f(xAp/lAp*rPe, yAp/lAp*rPe, zAp/lAp*rPe); // periapsis = |apoapsis| * periapsis_radius (from eccentricity)
        this.LOC_AP = localApoapsis;
        this.BODY = parentBody;
    }

    public Vector3f localPosition(float t) {
        float ra = LOC_AP.length();
        float rp = LOC_PE.length();
        float a = (ra + rp) * 0.5f;
        float e = ECC;
        float theta = t;

        float r = (a * (1 - e * e)) / (1 + e * (float)Math.cos(theta));

        Vector3f peDir = LOC_PE.normalize(new Vector3f());

        Vector3f basis = Math.abs(peDir.y) > 0.9f
            ? new Vector3f(1, 0, 0)
            : new Vector3f(0, 1, 0);

        Vector3f sideDir = peDir.cross(basis, new Vector3f()).normalize();

        float cos = (float)Math.cos(theta);
        float sin = (float)Math.sin(theta);

        return new Vector3f(peDir).mul(r * cos)
            .add(new Vector3f(sideDir).mul(r * sin));
    }

    public float averageRadius() {
        return (LOC_AP.length() + LOC_PE.length()) * 0.5f;
    }

    public Vector3f localEulerDegrees(Vector3f unitPosition, float t) {
        return new Vector3f(67, 67, 67);
    }
}
