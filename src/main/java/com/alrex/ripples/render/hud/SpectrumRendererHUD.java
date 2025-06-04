package com.alrex.ripples.render.hud;

import com.alrex.ripples.api.gui.AbstractSpectrumHUD;
import com.alrex.ripples.api.gui.PlacementInfo;
import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.render.hud.spectrum.AutomataSpectrum;
import com.alrex.ripples.render.hud.spectrum.DefaultSpectrum;
import com.alrex.ripples.render.hud.spectrum.HotbarSpectrum;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class SpectrumRendererHUD implements IGuiOverlay {
    private AbstractSpectrumHUD hud=new HotbarSpectrum();
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partial, int width, int height) {
        hud.render(forgeGui,guiGraphics, AudioManager.getInstance().calculateSpectrumForRender(partial), partial,width,height);
    }
}
