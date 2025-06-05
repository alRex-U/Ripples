package com.alrex.ripples.render.hud;

import com.alrex.ripples.api.gui.AbstractSpectrumRenderer;
import com.alrex.ripples.api.gui.SpectrumStyle;
import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.render.hud.spectrum.HotbarSpectrum;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class SpectrumRendererHUD implements IGuiOverlay {
    private AbstractSpectrumRenderer renderer =new HotbarSpectrum();

    public void setRenderer(AbstractSpectrumRenderer renderer) {
        this.renderer = renderer;
    }
    public AbstractSpectrumRenderer getRenderer(){
        return renderer;
    }

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partial, int width, int height) {
        renderer.render(forgeGui,guiGraphics, AudioManager.getInstance().calculateSpectrumForRender(partial), partial,width,height);
    }
}
