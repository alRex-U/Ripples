package com.alrex.ripples.api.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public abstract class AbstractSpectrumRenderer {
    public abstract void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float[] ft, float partialTick, int width, int height);
    public boolean acceptStyle(SpectrumStyle style){
        return true;
    }

    private PlacementInfo placementInfo=PlacementInfo.DEFAULT;
    private SpectrumStyle spectrumStyle=SpectrumStyle.DEFAULT;

    public SpectrumStyle getSpectrumStyle() {
        return spectrumStyle;
    }

    public void setSpectrumStyle(SpectrumStyle spectrumStyle) {
        if (this.acceptStyle(spectrumStyle))
            this.spectrumStyle = spectrumStyle;
    }

    public void setPlacementInfo(PlacementInfo placementInfo) {
        this.placementInfo = placementInfo;
    }

}
