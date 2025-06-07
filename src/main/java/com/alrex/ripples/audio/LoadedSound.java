package com.alrex.ripples.audio;

import com.alrex.ripples.audio.analyze.SignalReSampler;
import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

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

    public IAudioWaveProvider getInstance(Channel channel, SoundInstance sound){
        return new SoundProviderInstance(this,channel, sound);
    }

    private static class SoundProviderInstance implements IAudioWaveProvider {
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
        public float getGainFor(Vec3 listenerPos) {
            return switch (this.sound.getAttenuation()){
                case NONE -> this.sound.getVolume();
                case LINEAR -> {
                    float rollOffFactor =1.0f;
                    float referenceDistance=0.0f;
                    float maxDistance = Math.max(this.sound.getVolume(), 1.0F) * (float)sound.getSound().getAttenuationDistance();
                    double distance=new Vec3(sound.getX(),sound.getY(), sound.getZ()).distanceTo(listenerPos);
                    float attenuation= (float) (1 - rollOffFactor * (distance - referenceDistance) / (maxDistance - referenceDistance));
                    yield this.sound.getVolume()*attenuation;
                }
            };
        }

        @Override
        public float getPitch() {
            return sound.getPitch();
        }

        @Nullable
        @Override
        public Vec3 getPosition() {
            return new Vec3(sound.getX(),sound.getY(), sound.getZ());
        }

        @Override
        public void tick() {
            if (channel.playing())currentAudioTick++;
        }
    }
}
