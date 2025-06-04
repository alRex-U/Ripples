package com.alrex.ripples.audio;

import com.mojang.blaze3d.audio.Listener;

import javax.annotation.Nullable;
import javax.sound.sampled.AudioFormat;

public interface AudioWaveProvider {
    /// This method have to return values whose size is at least same or larger than <code>AudioManager.getDataSizeForOneTickFFT</code><br>
    /// Its frequency is 44100 Hz
    @Nullable
    short[] getCurrentWave();
    float getGainFor(Listener listener);
    default void tick(){}
}
