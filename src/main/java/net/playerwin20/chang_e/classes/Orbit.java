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

        // semi-major axis
        float a = (ra + rp) * 0.5f;
        float e = ECC;

        // orbital angle over time
        // t should probably be radians/sec * time
        float theta = t;

        // orbital radius at angle theta
        float r = (a * (1 - e * e)) / (1 + e * (float)Math.cos(theta));

        // orbit direction basis
        Vector3f peDir = LOC_PE.normalize(new Vector3f());

        // perpendicular axis in orbital plane
        Vector3f sideDir = new Vector3f(-peDir.z, peDir.y, peDir.x).normalize();

        // position in orbital plane
        Vector3f pos = new Vector3f(
            peDir.x * (float)Math.cos(theta) * r + sideDir.x * (float)Math.sin(theta) * r,
            peDir.y * (float)Math.cos(theta) * r + sideDir.y * (float)Math.sin(theta) * r,
            peDir.z * (float)Math.cos(theta) * r + sideDir.z * (float)Math.sin(theta) * r
        );
        return pos;
    }


    public Vector3f localEulerDegrees(Vector3f unitPosition, float t) {
        return new Vector3f(67, 67, 67);
    }
}
