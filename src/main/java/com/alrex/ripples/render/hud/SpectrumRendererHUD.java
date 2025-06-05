package com.alrex.ripples.render.hud;

import com.alrex.ripples.RipplesConfig;
import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.api.gui.AbstractSpectrumRenderer;
import com.alrex.ripples.api.gui.SpectrumStyle;
import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.render.hud.spectrum.HotbarSpectrum;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import javax.annotation.Nullable;

public class SpectrumRendererHUD implements IGuiOverlay {
    @Nullable
    private AbstractSpectrumRenderer renderer;

    public void setRenderer(AbstractSpectrumRenderer renderer) {
        this.renderer = renderer;
    }
    public AbstractSpectrumRenderer getRenderer(){
        if (renderer==null){
            renderer= RipplesSpectrumRegistry.get().getHUD(RipplesConfig.getSpectrumID());
        }
        return renderer;
    }

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partial, int width, int height) {
        getRenderer().render(forgeGui,guiGraphics, AudioManager.getInstance().calculateSpectrumForRender(partial), partial,width,height);
    }
}
