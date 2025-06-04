package com.alrex.ripples.audio;

import com.alrex.ripples.audio.analyze.SignalReSampler;
import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.Listener;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;
import java.util.List;

public class LoadedSound {
    private final List<short[]> soundData;
    private final ResourceLocation location;
    public LoadedSound(ResourceLocation location, ByteBuffer buffer, AudioFormat format){
        this.location=location;
        soundData= SignalReSampler.getAsMonoChannelSignalSequenceForEveryGameTick(
                format,
                buffer,
                AudioManager.DATA_SIZE_FOR_ONE_TICK_ANALYSIS,
                AudioManager.SAMPLE_RATE_FOR_ANALYSIS_SIGNAL
        );
    }

    public AudioWaveProvider getInstance(Channel channel, SoundInstance sound){
        return new SoundProviderInstance(this,channel, sound);
    }

    private static class SoundProviderInstance implements AudioWaveProvider{
        private final SoundInstance sound;
        private final Channel channel;
        private final LoadedSound data;
        private int currentAudioTick=-1;
        private SoundProviderInstance(LoadedSound data, Channel channel, SoundInstance sound){
            this.data=data;
            this.sound=sound;
            this.channel=channel;
        }

        @Nullable
        @Override
        public short[] getCurrentWave() {
            if (!channel.playing()) return null;
            if (currentAudioTick < 0 || data.soundData.size() <= currentAudioTick) return null;
            return data.soundData.get(currentAudioTick);
        }

        @Override
        public float getGainFor(Listener listener) {
            return 1;
        }

        @Override
        public void tick() {
            if (channel.playing())currentAudioTick++;
        }
    }
}
