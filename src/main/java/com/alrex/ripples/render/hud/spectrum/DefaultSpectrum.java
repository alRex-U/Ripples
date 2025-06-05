package com.alrex.ripples.render.hud.spectrum;

import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.api.gui.AbstractSpectrumRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class DefaultSpectrum extends AbstractSpectrumRenderer {
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float[] ft, float partialTick, int width, int height) {
        var data= AudioManager.getInstance().calculateSpectrumForRender(partialTick);
        if (data==null)return;
        int barCount=512;
        int barSampleSize= data.length/barCount;
        float[] spectrum= new float[barCount];

        for(var i=0;i<barCount;i++){
            float sum=0;
            for(int j=0;j<barSampleSize;j++){
                sum+=data[barSampleSize*i + j];
            }
            spectrum[i]=sum/barSampleSize;
        }
        int barWidth=(int)Math.ceil(width/(float)barCount);
        for (int i=0;i<barCount;i++){
            guiGraphics.fill(barWidth*i,0,barWidth*(i+1), (int) (20*spectrum[i] * height / 2f),0x99FFFFFF);
        }
    }
}
