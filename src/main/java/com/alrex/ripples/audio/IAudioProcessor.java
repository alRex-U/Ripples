package com.alrex.ripples.audio;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import javax.annotation.Nullable;
import java.util.Collection;

public interface IAudioProcessor {
    void tick(Collection<AudioWaveProvider> providers);
    void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partial, int width, int height);
    void notifyConfigUpdated();
}
