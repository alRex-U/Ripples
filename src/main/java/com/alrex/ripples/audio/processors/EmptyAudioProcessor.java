package com.alrex.ripples.audio.processors;

import com.alrex.ripples.audio.IAudioWaveProvider;
import com.alrex.ripples.audio.IAudioProcessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import java.util.Collection;

@OnlyIn(Dist.CLIENT)
public class EmptyAudioProcessor implements IAudioProcessor {
    @Override
    public void tick(Collection<IAudioWaveProvider> providers) {
        providers.forEach(IAudioWaveProvider::tick);
    }

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partial, int width, int height) {

    }

    @Override
    public void notifyConfigUpdated() {

    }
}
