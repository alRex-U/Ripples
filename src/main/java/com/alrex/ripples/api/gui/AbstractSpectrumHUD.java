package com.alrex.ripples.api.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public abstract class AbstractSpectrumHUD implements IGuiOverlay {
    public abstract void render(ForgeGui forgeGui, GuiGraphics guiGraphics, PlacementInfo position , float partialTick, int width, int height);

    private PlacementInfo placementInfo=PlacementInfo.DEFAULT;

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float v, int i, int i1) {
        render(forgeGui,guiGraphics, placementInfo,v,i,i1);
    }

    public void setPlacementInfo(PlacementInfo placementInfo) {
        this.placementInfo = placementInfo;
    }
}
