package com.alrex.ripples.render.hud.spectrum;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.api.gui.AbstractSpectrumHUD;
import com.alrex.ripples.api.gui.PlacementInfo;
import com.alrex.ripples.util.RenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class HotbarSpectrum extends AbstractSpectrumHUD {
    public static final ResourceLocation SPECTRUM_ID=new ResourceLocation(Ripples.MOD_ID,"hotbar");
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float[] ft, float partialTick, int width, int height) {
        int baseY=height-22;

        int hotBarWidth=20*9;
        float baseX=(width-hotBarWidth)/2f;
        float barWidth=hotBarWidth/(float)ft.length;
        switch (getSpectrumStyle()){
            case BLOCKS -> {
                for(var i=0;i<ft.length;i++){
                    RenderUtil.fillWithFloatPos(
                            guiGraphics,
                            baseX+barWidth*i,
                            (float) baseY,
                            baseX+barWidth*(i+1),
                            (float) Mth.lerp(Math.min(Math.log(ft[i]*100f+1),1f),baseY,baseY-20),
                            -1,
                            0x55FFFFFF
                    );
                }
            }
            case LINE -> {
                float[] x=new float[ft.length],y=new float[ft.length];
                for(var i=0;i<ft.length;i++){
                    x[i]= baseX+barWidth*i;
                    y[i]= (float) Mth.lerp(Math.min(Math.log(ft[i]*100f+1),1f),baseY,baseY-20);
                }
                RenderUtil.drawPolyline(
                        guiGraphics,
                        x,y,-1,
                        0x55FFFFFF
                );
            }
        }

    }
}
