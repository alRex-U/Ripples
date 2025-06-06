package com.alrex.ripples.mixin.audio;

import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.audio.AudioWaveProvider;
import com.alrex.ripples.audio.analyze.SignalReSampler;
import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.Listener;
import com.mojang.blaze3d.audio.OpenAlUtil;
import com.mojang.blaze3d.audio.SoundBuffer;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.openal.AL10;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentSkipListMap;

@Mixin(Channel.class)
public abstract class ChannelMixin {

    @Shadow @Nullable private AudioStream stream;

    @Shadow private int streamingBufferSize;

    @Shadow @Final private static Logger LOGGER;
    @Shadow @Final private int source;

    @Shadow
    private static int calculateBufferSize(AudioFormat p_83661_, int p_83662_) {
        return 0;
    }


    // Custom Fields
    @Nullable
    @Unique
    private Vec3 ripples$position;
    @Unique
    private boolean ripples$streaming;
    @Unique boolean ripples$streamFinished;
    @Unique
    private ConcurrentSkipListMap<Integer,short[]> bufferIDToData=new ConcurrentSkipListMap<>();
    @Unique
    @Nullable
    private short[] ripples$currentWave =null;
    @Unique
    private int ripples$currentBufID =0;
    @Unique
    private ByteBuffer ripples$audioDataStreamBuffer =ByteBuffer.allocate(0);
    @Unique
    private ByteBuffer ripples$tempBuffer =ByteBuffer.allocate(0);
    @Unique
    private int ripples$singleBufferSize =AudioManager.SAMPLE_RATE_FOR_ANALYSIS_SIGNAL/21;
    @Unique
    private float ripples$volume =1.0f;
    @Unique
    private boolean ripples$attenuating=false;
    @Unique
    private float ripples$rollOffFactor =1.0f;
    @Unique
    private float ripples$referenceDistance=0.0f;
    @Unique
    private float ripples$maxDistance=16f;

    @Inject(method = "setSelfPosition",at=@At("HEAD"))
    public void onSetSelfPosition(Vec3 p_83655_, CallbackInfo ci){
        ripples$position =p_83655_;
    }
    @Inject(method = "play", at=@At("HEAD"))
    public void onPlay(CallbackInfo ci){
        if (ripples$streaming) {
            ripples$streamFinished = false;
            final Channel channel = (Channel) (Object) this;
            AudioManager.getInstance().registerAudioSource(this.source, new AudioWaveProvider() {
                @Nullable
                @Override
                public short[] getCurrentWave() {
                    if (!channel.playing()) return null;
                    return ripples$currentWave;
                }

                @Nullable
                @Override
                public Vec3 getPosition() {
                    return ripples$position;
                }

                @Override
                public float getGainFor(Vec3 listenerPos) {
                    float attenuation;
                    if (ripples$attenuating && ripples$position !=null){
                        double distance=listenerPos.distanceTo(ripples$position);
                        // formula of OpenAL's linear attenuation
                        attenuation= (float) (1 - ripples$rollOffFactor * (distance - ripples$referenceDistance) / (ripples$maxDistance - ripples$referenceDistance));
                    }else {
                        attenuation=1f;
                    }
                    return Mth.clamp(ripples$volume*attenuation,0f,1f);
                }
            });
        }
    }

    @Inject(method = "setVolume",at=@At("HEAD"))
    public void onSetVolume(float volume, CallbackInfo ci){
        this.ripples$volume=volume;
    }
    @Inject(method = "disableAttenuation",at=@At("HEAD"))
    public void onDisableAttenuation(CallbackInfo ci){
        ripples$attenuating=false;
    }


    @Inject(method = "linearAttenuation",at=@At("HEAD"))
    public void onLinearAttenuation(float attenuationDistance, CallbackInfo ci){
        ripples$attenuating=true;
        ripples$maxDistance=attenuationDistance;
        ripples$rollOffFactor =1.0f;
        ripples$referenceDistance=0.0f;
    }

    @Inject(method = "attachStaticBuffer",at=@At("HEAD"))
    public void onAttachStaticBuffer(SoundBuffer soundBuffer, CallbackInfo ci) {
        this.ripples$streaming=false;
    }

    /**
     * @author alRex-U
     * @reason
     */
    @Overwrite
    public void attachBufferStream(AudioStream stream) {
        this.stream=stream;
        AudioFormat format = stream.getFormat();
        this.streamingBufferSize = calculateBufferSize(format,1);

        // 21 in the below formula is Minecraft's game tick rate 20Hz with safety margin 1
        this.ripples$streaming =true;
        this.ripples$singleBufferSize = (~0b11) & (int) ((format.getSampleSizeInBits()/8f)*format.getChannels()*format.getSampleRate()/21f);

        this.pumpBuffers(40);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void pumpBuffers(int pumpCount) {
        if (this.stream != null) {
            try {
                if (ripples$tempBuffer.capacity() < ripples$singleBufferSize){
                    ripples$tempBuffer =ByteBuffer.allocateDirect(ripples$singleBufferSize);
                }
                for(int i = 0; i < pumpCount; ++i) {
                    ripples$tempBuffer.clear();
                    while(true){
                        if (ripples$audioDataStreamBuffer.remaining() <= ripples$tempBuffer.remaining()) {
                            ripples$tempBuffer.put(ripples$audioDataStreamBuffer);
                        }else if (ripples$audioDataStreamBuffer.hasRemaining()){
                            int remaining= ripples$tempBuffer.remaining();
                            ripples$tempBuffer.put(ripples$tempBuffer.position(), ripples$audioDataStreamBuffer, ripples$audioDataStreamBuffer.position(),remaining);
                            ripples$tempBuffer.position(ripples$tempBuffer.position()+remaining);
                            ripples$audioDataStreamBuffer.position(ripples$audioDataStreamBuffer.position()+remaining);
                        }

                        if (!ripples$audioDataStreamBuffer.hasRemaining()){
                            if (ripples$streamFinished){
                                ripples$tempBuffer.flip();
                                break;
                            }
                            ripples$audioDataStreamBuffer = this.stream.read(this.streamingBufferSize);
                            if (ripples$audioDataStreamBuffer == null) {
                                ripples$tempBuffer.flip();
                                break;
                            }
                            if (ripples$audioDataStreamBuffer.remaining() < streamingBufferSize){
                                ripples$streamFinished =true;
                            }
                        }
                        if (!ripples$tempBuffer.hasRemaining()){
                            ripples$tempBuffer.rewind();
                            break;
                        }
                    }
                    if (!ripples$tempBuffer.hasRemaining())return;
                    (new SoundBuffer(ripples$tempBuffer, this.stream.getFormat())).releaseAlBuffer().ifPresent((bufID) -> {
                        ripples$tempBuffer.rewind();
                        bufferIDToData.put(
                                bufID,
                                SignalReSampler.resample(
                                        SignalReSampler.getAsMonoChannelSignalArray(
                                                this.stream.getFormat(),
                                                ripples$tempBuffer
                                        ),
                                        (int)this.stream.getFormat().getSampleRate(),
                                        AudioManager.SAMPLE_RATE_FOR_ANALYSIS_SIGNAL,
                                        AudioManager.DATA_SIZE_FOR_ONE_TICK_ANALYSIS
                                )
                        );
                        AL10.alSourceQueueBuffers(this.source, new int[]{bufID});
                    });
                }
            } catch (IOException e) {
                LOGGER.error("Failed to read from audio stream", e);
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private int removeProcessedBuffers() {
        int removeCount = AL10.alGetSourcei(this.source, AL10.AL_BUFFERS_PROCESSED);
        // currentBufID = AL10.alGetSourcei(this.source,AL10.AL_BUFFER);
        if (removeCount > 0) {
            int[] removedBuffers = new int[removeCount];
            AL10.alSourceUnqueueBuffers(this.source, removedBuffers);

            ripples$currentBufID =removedBuffers[removeCount-1];
            ripples$currentWave =bufferIDToData.get(ripples$currentBufID);
            for (int buf:removedBuffers){
                bufferIDToData.remove(buf);
            }

            OpenAlUtil.checkALError("Unqueue buffers");
            AL10.alDeleteBuffers(removedBuffers);
            OpenAlUtil.checkALError("Remove processed buffers");
        }

        return removeCount;
    }

    @Inject(method = "destroy",at = @At("HEAD"))
    public void onDestroy(CallbackInfo ci){
        AudioManager.getInstance().removeAudioSource(this.source);
    }
}
