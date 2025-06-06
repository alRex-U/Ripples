package com.alrex.ripples.api.audio;

public record RelativeAngleInFOV(float angleRadian, float distanceRadianFromPointOfInterest) {
    public static final RelativeAngleInFOV EXACT_FRONT = new RelativeAngleInFOV(0, 0);
}
