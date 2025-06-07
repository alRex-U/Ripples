package com.alrex.ripples.audio;

import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public interface IAudioWaveProvider {
    /// This method have to return values whose size is at least same or larger than <code>AudioManager.getDataSizeForOneTickFFT</code><br>
    /// Its frequency is 44100 Hz
    @Nullable
    short[] getCurrentWave();
    float getGainFor(Vec3 listenerPos);
    float getPitch();
    default void tick(){}
    @Nullable
    Vec3 getPosition();
}
