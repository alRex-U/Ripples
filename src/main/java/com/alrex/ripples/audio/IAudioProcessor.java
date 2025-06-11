package com.alrex.ripples.audio;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import java.util.Collection;

@OnlyIn(Dist.CLIENT)
public interface IAudioProcessor {
    void tick(Collection<IAudioWaveProvider> providers);
    void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partial, int width, int height);
    void notifyConfigUpdated();
}
