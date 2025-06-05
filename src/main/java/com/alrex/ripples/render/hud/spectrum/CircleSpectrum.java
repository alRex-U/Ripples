package com.alrex.ripples.render.hud.spectrum;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.api.gui.AbstractSpectrumRenderer;
import com.alrex.ripples.render.RenderUtil;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.joml.Matrix4f;

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
                /*
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

                 */

                Matrix4f matrix4f = guiGraphics.pose().last().pose();
                var renderType= RenderUtil.RenderTypes.lines();
                float a = (float) FastColor.ARGB32.alpha(color) / 255.0F;
                float r = (float) FastColor.ARGB32.red(color) / 255.0F;
                float g = (float) FastColor.ARGB32.green(color) / 255.0F;
                float b = (float) FastColor.ARGB32.blue(color) / 255.0F;
                VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
                double baseAngle=2.*Math.PI/(2*ft.length);
                for(var i=0;i<ft.length;i++){
                    double power=Mth.lerp(Math.min(Math.log(ft[i]*100f+1),1f),0,radius/3f);
                    double angle=-Math.PI/2. + baseAngle*i;
                    float x= offsetX+(float) ((radius+power)*(Math.cos(angle)));
                    float y= offsetY+(float) ((radius+power)*(Math.sin(angle)));
                    vertexconsumer.vertex(matrix4f,x,y,-1).color(r,g,b,a).endVertex();
                }
                for(var i=ft.length;i<2*ft.length;i++){
                    double power=Mth.lerp(Math.min(Math.log(ft[2* ft.length-1-i]*100f+1),1f),0,radius/3f);
                    double angle=-Math.PI/2. + baseAngle*i;
                    float x= offsetX+(float) ((radius+power)*(Math.cos(angle)));
                    float y= offsetY+(float) ((radius+power)*(Math.sin(angle)));
                    vertexconsumer.vertex(matrix4f,x,y,-1).color(r,g,b,a).endVertex();
                }
                guiGraphics.flush();
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
