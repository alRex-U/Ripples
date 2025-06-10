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

    private final Supplier<IAudioProcessor> ctor;
    private final String translationKey;
    RenderContent(Supplier<IAudioProcessor> ctor){
        this.ctor=ctor;
        this.translationKey="ripples.content."+this.name().toLowerCase();
    }

    public IAudioProcessor newProcessor(){
        return ctor.get();
    }

    public String getTranslationKey() {
        return translationKey;
    }
}
