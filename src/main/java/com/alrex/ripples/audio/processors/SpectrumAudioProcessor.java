package com.alrex.ripples.audio.processors;

import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.api.gui.AbstractSpectrumRenderer;
import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.audio.AudioWaveProvider;
import com.alrex.ripples.audio.IAudioProcessor;
import com.alrex.ripples.audio.analyze.FFT;
import com.alrex.ripples.audio.analyze.WindowFunction;
import com.alrex.ripples.config.RipplesConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import javax.annotation.Nullable;
import java.util.Collection;

public class SpectrumAudioProcessor implements IAudioProcessor {
    @Nullable
    private AbstractSpectrumRenderer renderer;
    @Nullable
    private float[] spectrumInPreviousTick;
    @Nullable
    private float[] spectrumInCurrentTick;

    private final WindowFunction window=new WindowFunction.Hamming();


    public AbstractSpectrumRenderer getRenderer(){
        if (renderer==null){
            renderer= RipplesSpectrumRegistry.get().getSpectrum(RipplesConfig.getSpectrumID());
        }
        return renderer;
    }

    @Override
    public void tick(Collection<AudioWaveProvider> providers) {
        var sumWave=new float[AudioManager.DATA_SIZE_FOR_ONE_TICK_ANALYSIS];
        float scale= 1f / (Short.MAX_VALUE);
        var listenerPos= Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        Double spectrumGainValue=RipplesConfig.SPECTRUM_GAIN.get();
        if (!providers.isEmpty() && spectrumGainValue != null) {
            float spectrumGain=spectrumGainValue.floatValue();
            for (var audioProvider : providers) {
                audioProvider.tick();
                var wave = audioProvider.getCurrentWave();
                var soundGain=audioProvider.getGainFor(listenerPos);
                if (wave == null) continue;
                if (wave.length < sumWave.length) continue;
                for (int i = 0; i < sumWave.length; i++) {
                    sumWave[i] += spectrumGain * soundGain * scale * wave[i];
                }
            }
        }

        spectrumInPreviousTick=spectrumInCurrentTick;
        window.apply(sumWave);
        var fft= FFT.magnitude(sumWave);
        spectrumInCurrentTick=clipArray(fft, (int) (fft.length*RipplesConfig.CLIP_FT_SIZE.get()));
    }

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partial, int width, int height) {
        getRenderer().render(forgeGui,guiGraphics, calculateSpectrumForRender(partial), partial,width,height);
    }

    @Override
    public void notifyConfigUpdated() {
        renderer= RipplesSpectrumRegistry.get().getSpectrum(RipplesConfig.getSpectrumID());
    }



    private float[] clipArray(float[] original,int size){
        if (size>= original.length)return original;
        float[] newArray=new float[size];
        System.arraycopy(original, 0, newArray, 0, newArray.length);
        return newArray;
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
        partialTick= Mth.clamp(partialTick,0,1);
        var buf=new float[Math.min(spectrumInCurrentTick.length,spectrumInPreviousTick.length)];
        for (int i=0;i< buf.length;i++){
            buf[i]= Mth.lerp(partialTick,spectrumInPreviousTick[i],spectrumInCurrentTick[i]);
        }
        return buf;
    }
}
