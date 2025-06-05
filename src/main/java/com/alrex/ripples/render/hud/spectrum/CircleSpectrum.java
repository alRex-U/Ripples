package com.alrex.ripples.render.hud.spectrum;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.api.gui.AbstractSpectrumRenderer;
import com.alrex.ripples.render.RenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class CircleSpectrum extends AbstractSpectrumRenderer {
    public static final ResourceLocation SPECTRUM_ID=new ResourceLocation(Ripples.MOD_ID,"circle");
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float[] ft, float partialTick, int width, int height) {
        float offsetX=width/2f;
        float offsetY=height/2f;
        float radius=width/7f;

        int opacity=getOpacityInt();
        int color= FastColor.ARGB32.color(opacity,0xFF,0xFF,0xFF);
        switch (getSpectrumStyle()){
            case DEFAULT -> {
                float[] x=new float[ft.length],y=new float[ft.length];
                double baseAngle=2.*Math.PI/ft.length;
                for(var i=0;i<ft.length;i++){
                    double power=Mth.lerp(Math.min(Math.log(ft[i]*100f+1),1f),0,radius/3f);
                    double angle=-Math.PI/2. + baseAngle*i;
                    x[i]= offsetX+(float) ((radius+power)*(Math.cos(angle)));
                    y[i]= offsetY+(float) ((radius+power)*(Math.sin(angle)));
                }
                RenderUtil.drawPolyline(
                        guiGraphics,
                        x,y,-1,
                        color
                );
            }
            case POINTS -> {
                double baseAngle=2.*Math.PI/ft.length;
                for(var i=0;i<ft.length;i++){
                    double power=Mth.lerp(Math.min(Math.log(ft[i]*100f+1),1f),0,radius/3f);
                    double angle=-Math.PI/2. + baseAngle*i;
                    float x= offsetX+(float) ((radius+power)*(Math.cos(angle)));
                    float y= offsetY+(float) ((radius+power)*(Math.sin(angle)));
                    RenderUtil.fillWithFloatPos(
                            guiGraphics,
                            x-0.25f, y-0.25f,
                            x+0.25f,y+0.25f,
                            -1,
                            color
                    );
                }
            }
        }
    }
}
