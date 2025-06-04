package com.alrex.ripples.audio;

import com.alrex.ripples.audio.analyze.FFT;
import com.alrex.ripples.audio.analyze.WindowFunction;
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
    private float[] spectrumInPreviousTick;
    @Nullable
    private float[] spectrumInCurrentTick;

    private final WindowFunction window=new WindowFunction.Hamming();

    public void registerAudioSource(int sourceID, AudioWaveProvider provider){
        channelToDataSuppliers.put(sourceID,provider);
    }

    public void removeAudioSource(int sourceID){
        channelToDataSuppliers.remove(sourceID);
    }

    public void tick(){

        var sumWave=new float[DATA_SIZE_FOR_ONE_TICK_ANALYSIS];
        float scale= 1f / (Short.MAX_VALUE);
        var values=channelToDataSuppliers.values();
        if (!values.isEmpty()) {
            for (var audioProvider : values) {
                audioProvider.tick();
                var wave = audioProvider.getCurrentWave();
                //var gain=audioProvider.getGainFor();
                var gain = 1;
                if (wave == null) continue;
                if (wave.length < sumWave.length) continue;
                for (int i = 0; i < sumWave.length; i++) {
                    sumWave[i] += gain * scale * wave[i];
                }
            }
        }

        spectrumInPreviousTick=spectrumInCurrentTick;
        window.apply(sumWave);
        spectrumInCurrentTick=FFT.magnitude(sumWave);
    }

    @Nullable
    public float[] calculateSpectrumForRender(float partialTick){
        if (spectrumInPreviousTick==null || spectrumInCurrentTick==null){
            if (spectrumInCurrentTick != null){
                return spectrumInCurrentTick;
            }else {
                return null;
            }
        }
        partialTick=Mth.clamp(partialTick,0,1);
        var buf=new float[Math.min(spectrumInCurrentTick.length,spectrumInPreviousTick.length)];
        for (int i=0;i< buf.length;i++){
            buf[i]= Mth.lerp(partialTick,spectrumInPreviousTick[i],spectrumInCurrentTick[i]);
        }
        return buf;
    }

}
