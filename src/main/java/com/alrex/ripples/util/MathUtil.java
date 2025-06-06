package com.alrex.ripples.util;

public class MathUtil {
    public static float normalizeRadian(float angle) {
        return (float) (angle - 2 * Math.PI * Math.floor((angle + Math.PI) / (2. * Math.PI)));
    }

    public static float normalizeDegree(float angle) {
        return (float) (angle - 360f * Math.floor((angle + 180f) / 360f));
    }
}
