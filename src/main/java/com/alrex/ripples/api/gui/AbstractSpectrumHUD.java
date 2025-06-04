package com.alrex.ripples.api.gui;

import com.alrex.ripples.audio.AudioManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public abstract class AbstractSpectrumHUD{
    public abstract void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float[] ft, float partialTick, int width, int height);

    private PlacementInfo placementInfo=PlacementInfo.DEFAULT;
    private SpectrumStyle spectrumStyle=SpectrumStyle.BLOCKS;

    public SpectrumStyle getSpectrumStyle() {
        return spectrumStyle;
    }

    public void setPlacementInfo(PlacementInfo placementInfo) {
        this.placementInfo = placementInfo;
    }
}
