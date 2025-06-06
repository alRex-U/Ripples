package com.alrex.ripples.render;

import com.alrex.ripples.audio.IAudioProcessor;
import com.alrex.ripples.audio.processors.EmptyAudioProcessor;
import com.alrex.ripples.audio.processors.SoundMapAudioProcessor;
import com.alrex.ripples.audio.processors.SpectrumAudioProcessor;

import java.util.function.Supplier;

public enum RenderContent {
    NONE(EmptyAudioProcessor::new),
    SPECTRUM(SpectrumAudioProcessor::new),
    SOUND_MAP(SoundMapAudioProcessor::new);

    private Supplier<IAudioProcessor> ctor;
    RenderContent(Supplier<IAudioProcessor> ctor){
        this.ctor=ctor;
    }

    public IAudioProcessor newProcessor(){
        return ctor.get();
    }
}
