package com.alrex.ripples.audio;

import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.audio.analyze.FFT;
import com.alrex.ripples.audio.analyze.WindowFunction;
import com.alrex.ripples.render.hud.HUDRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentSkipListMap;

public class AudioManager {
    public static final int SAMPLE_RATE_FOR_ANALYSIS_SIGNAL=44100;
    // Assuming the sample rate is 44100 Hz, the data size of 1 tick (1/20 sec) is 2205
    // 2048 is the largest value among pow of 2 under 2205
    public static final int DATA_SIZE_FOR_ONE_TICK_ANALYSIS =2048;

    private AudioManager(){}
    private static final AudioManager instance=new AudioManager();

    public static AudioManager getInstance() {
        return instance;
    }

    private final ConcurrentSkipListMap<Integer, AudioWaveProvider> channelToDataSuppliers=new ConcurrentSkipListMap<>();
    @Nullable
    private IAudioProcessor processor;

    public void registerAudioSource(int sourceID, AudioWaveProvider provider){
        channelToDataSuppliers.put(sourceID,provider);
    }

    public void removeAudioSource(int sourceID){
        channelToDataSuppliers.remove(sourceID);
    }

    public IAudioProcessor getAudioProcessor() {
        if (processor==null){
            processor=RipplesConfig.CONTENT_TYPE.get().newProcessor();
        }
        return processor;
    }

    public void tick(){
        getAudioProcessor().tick(channelToDataSuppliers.values());
    }

    public void notifyConfigChanged(){
        getAudioProcessor().notifyConfigUpdated();
    }

}
