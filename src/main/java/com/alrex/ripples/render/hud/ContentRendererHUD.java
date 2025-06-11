package com.alrex.ripples.render.hud;

import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.api.gui.AbstractSpectrumRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ContentRendererHUD implements IGuiOverlay {
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partial, int width, int height) {
        AudioManager.getInstance().getAudioProcessor().render(forgeGui, guiGraphics, partial, width, height);
    }
}
