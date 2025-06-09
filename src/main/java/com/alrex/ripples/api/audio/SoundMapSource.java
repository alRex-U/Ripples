package com.alrex.ripples.api.audio;

import com.alrex.ripples.util.MathUtil;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;

public record SoundMapSource(float soundPressure,@Nullable RelativeAngleInFOV relativeAngle) {
    public static SoundMapSource lerp(float partial, @Nullable SoundMapSource previous, @Nullable SoundMapSource next) {
        if (previous == null && next == null) return new SoundMapSource(0, null);
        if (previous == null) previous = new SoundMapSource(0, next.relativeAngle());
        if (next == null) next = new SoundMapSource(0, previous.relativeAngle());
        if (previous.relativeAngle()==null && next.relativeAngle()==null)return new SoundMapSource(Mth.lerp(partial,previous.soundPressure(),next.soundPressure()), null);
        if (previous.relativeAngle() == null)previous=new SoundMapSource(previous.soundPressure(),next.relativeAngle());
        if (next.relativeAngle()==null)next=new SoundMapSource(next.soundPressure(), previous.relativeAngle());

        float newSoundPressure = Mth.lerp(partial, previous.soundPressure(), next.soundPressure());
        var angle = new RelativeAngleInFOV(
                MathUtil.normalizeRadian(
                        previous.relativeAngle().angleRadian()
                                + MathUtil.normalizeRadian(
                                partial * (next.relativeAngle().angleRadian() - previous.relativeAngle().angleRadian())
                        )
                ),
                Mth.lerp(partial, previous.relativeAngle().distanceRadianFromPointOfInterest(), next.relativeAngle().distanceRadianFromPointOfInterest())
        );
        return new SoundMapSource(newSoundPressure, angle);
    }
}
