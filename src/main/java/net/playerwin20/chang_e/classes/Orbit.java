package net.playerwin20.chang_e.classes;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public class Orbit { 
    public float A, E, I, O, W, V;
    public Vector3d REFERENCE;
    public OrbitTransition[] TRANSITION;

    private class OrbitTransition {
        public Orbit targetOrbit;
        public float transitionTime;
    }

    public Orbit(
        float semiMajorAxis,
        float eccentricity,
        float inclination,
        float longitudeOfAscendingNode,
        float argumentOfPeriapsis,
        float meanAnomaly,
        @Nullable Vector3d reference,
        @Nullable OrbitTransition[] transition
    )
    {
        this.A = semiMajorAxis;
        this.E = eccentricity;
        this.I = inclination;
        this.O = longitudeOfAscendingNode;
        this.W = argumentOfPeriapsis;
        this.V = meanAnomaly;
        this.REFERENCE = reference;
        this.TRANSITION = transition;
    }

    private static final float SECONDS_PER_YEAR = 365.25f * 24f * 3600f;

    public Vector3d getPosition(float deltaT) {
        // A is expressed in astronomical units (AU).
        // deltaT is expected in seconds, so convert to years for Gaussian orbital units.
        float deltaTYears = deltaT / SECONDS_PER_YEAR;
        float meanMotion = (float) (2.0 * Math.PI / Math.sqrt(A * A * A));
        float M = V + deltaTYears * meanMotion;
        float twoPi = (float) (2.0 * Math.PI);
        M = M % twoPi;
        if (M < 0) {
            M += twoPi;
        }

        float eccentricAnomaly = M;
        for (int i = 0; i < 20; i++) {
            float f = eccentricAnomaly - E * (float) Math.sin(eccentricAnomaly) - M;
            float fPrime = 1 - E * (float) Math.cos(eccentricAnomaly);
            eccentricAnomaly -= f / fPrime;
        }

        float x = A * ((float) Math.cos(eccentricAnomaly) - E);
        float y = A * (float) Math.sqrt(1 - E * E) * (float) Math.sin(eccentricAnomaly);

        // Rotate to 3D space using inclination, longitude of ascending node, and argument of periapsis
        float cosO = (float) Math.cos(O);
        float sinO = (float) Math.sin(O);
        float cosI = (float) Math.cos(I);
        float sinI = (float) Math.sin(I);
        float cosW = (float) Math.cos(W);
        float sinW = (float) Math.sin(W);

        float X = x * (cosO * cosW - sinO * sinW * cosI) - y * (cosO * sinW + sinO * cosW * cosI);
        float Y = x * (sinO * cosW + cosO * sinW * cosI) + y * (-sinO * sinW + cosO * cosW * cosI);
        float Z = x * (sinW * sinI) + y * (cosW * sinI);

        return new Vector3d(X, Y, Z);
    }

    public Vector3d globalPosition(float deltaT) {
        if(REFERENCE == null) return getPosition(deltaT);
        else {
            return new Vector3d(getPosition(deltaT)).add(REFERENCE);
        }
    }
}
