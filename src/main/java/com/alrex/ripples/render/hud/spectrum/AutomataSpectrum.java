package com.alrex.ripples.render.hud.spectrum;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.api.gui.AbstractSpectrumHUD;
import com.alrex.ripples.api.gui.PlacementInfo;
import com.alrex.ripples.audio.analyze.SignalReSampler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class AutomataSpectrum extends AbstractSpectrumHUD {
    public static final ResourceLocation SPECTRUM_ID=new ResourceLocation(Ripples.MOD_ID,"automata");

    private static final int BASE_COLOR=0xFFCDC8b0;
    private static final int ACCENT_COLOR=0xFF878779;
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float[] ft, float partialTick, int width, int height) {
        int barCount=20;
        var data= SignalReSampler.decreaseSize(ft,barCount);
        int margin=4;
        int componentWidth= (20 + 19)*2;
        int componentHeight= 30;
        int offsetX=width-margin-componentWidth;
        int offsetY=margin;

        guiGraphics.fill(
                offsetX,
                offsetY+componentHeight-2,
                offsetX+2,
                offsetY+componentHeight,
                BASE_COLOR
        );
        guiGraphics.fill(
                offsetX+componentWidth-2,
                offsetY+componentHeight-2,
                offsetX+componentWidth,
                offsetY+componentHeight,
                BASE_COLOR
        );
        guiGraphics.fill(
                offsetX,
                offsetY+componentHeight-4,
                offsetX+componentWidth,
                offsetY+componentHeight-3,
                BASE_COLOR
        );
        guiGraphics.fill(
                offsetX,
                offsetY+componentHeight-6,
                offsetX+componentWidth,
                offsetY+componentHeight-5,
                ACCENT_COLOR
        );
        for (var i=0;i<barCount;i++){
            guiGraphics.fill(
                    offsetX + i*4,
                    offsetY+componentHeight-10,
                    offsetX+i*4+2,
                    offsetY+componentHeight-8,
                    ACCENT_COLOR
            );
            guiGraphics.fill(
                    offsetX + i*4,
                    (int)Mth.lerp(Math.min(Math.log(data[i]*200f+1),1f),offsetY+componentHeight-10,offsetY),
                    offsetX+i*4+2,
                    offsetY+componentHeight-10,
                    BASE_COLOR
            );
        }
    }
}
