package com.alrex.ripples.api.gui;

import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.hud.ColorPallet;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public abstract class AbstractSpectrumRenderer {
    public abstract void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float[] ft, float partialTick, int width, int height);
    public boolean acceptStyle(SpectrumStyle style){
        return true;
    }


    public SpectrumStyle getSpectrumStyle() {
        return RipplesConfig.SPECTRUM_STYLE.get();
    }

    public double getOpacity(){
        return RipplesConfig.SPECTRUM_OPACITY.get();
    }
    public int getOpacityInt(){
        return (int) (255.*RipplesConfig.SPECTRUM_OPACITY.get());
    }
    public ColorPallet getColorPallet(){
        return RipplesConfig.getColorPallet();
    }

    public void setPlacementInfo(PlacementInfo placementInfo) {
    }

}
