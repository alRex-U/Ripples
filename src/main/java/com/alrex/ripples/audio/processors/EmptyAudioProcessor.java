package com.alrex.ripples.audio.processors;

import com.alrex.ripples.audio.AudioWaveProvider;
import com.alrex.ripples.audio.IAudioProcessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import java.util.Collection;

public class EmptyAudioProcessor implements IAudioProcessor {
    @Override
    public void tick(Collection<AudioWaveProvider> providers) {
        providers.forEach(AudioWaveProvider::tick);
    }

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partial, int width, int height) {

    }

    @Override
    public void notifyConfigUpdated() {

    }
}
