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

        var pallet=getColorPallet();
        switch (getSpectrumStyle()){
            case DEFAULT -> {
                Matrix4f matrix4f = guiGraphics.pose().last().pose();
                var renderType= RenderUtil.RenderTypes.lines();
                VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
                double baseAngle=2.*Math.PI/(2*ft.length);
                float a = (float) getOpacity();
                for(var i=0;i<ft.length;i++){
                    double power=Mth.lerp(Math.min(Math.log(ft[i]*100f+1),1f),0,radius/3f);

                    int color=pallet.getColor((float) power);
                    float r = (float) FastColor.ARGB32.red(color) / 255.0F;
                    float g = (float) FastColor.ARGB32.green(color) / 255.0F;
                    float b = (float) FastColor.ARGB32.blue(color) / 255.0F;

                    double angle=-Math.PI/2. + baseAngle*i;
                    float x= offsetX+(float) ((radius+power)*(Math.cos(angle)));
                    float y= offsetY+(float) ((radius+power)*(Math.sin(angle)));
                    vertexconsumer.vertex(matrix4f,x,y,-1).color(r,g,b,a).endVertex();
                }
                for(var i=ft.length;i<2*ft.length;i++){
                    double power=Mth.lerp(Math.min(Math.log(ft[2* ft.length-1-i]*100f+1),1f),0,radius/3f);

                    int color=pallet.getColor((float) power);
                    float r = (float) FastColor.ARGB32.red(color) / 255.0F;
                    float g = (float) FastColor.ARGB32.green(color) / 255.0F;
                    float b = (float) FastColor.ARGB32.blue(color) / 255.0F;

                    double angle=-Math.PI/2. + baseAngle*i;
                    float x= offsetX+(float) ((radius+power)*(Math.cos(angle)));
                    float y= offsetY+(float) ((radius+power)*(Math.sin(angle)));
                    vertexconsumer.vertex(matrix4f,x,y,-1).color(r,g,b,a).endVertex();
                }
                guiGraphics.flush();
            }
            case POINTS -> {
                double baseAngle=2.*Math.PI/(2*ft.length);
                int opacity=getOpacityInt()<<24;
                for(var i=0;i<ft.length;i++){
                    double power=Mth.lerp(Math.min(Math.log(ft[i]*100f+1),1f),0,radius/3f);

                    int color=(pallet.getColor((float) power)&0x00FFFFFF)|opacity;

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
                for(var i=ft.length;i<2*ft.length;i++){
                    double power=Mth.lerp(Math.min(Math.log(ft[2* ft.length-1-i]*100f+1),1f),0,radius/3f);

                    int color=(pallet.getColor((float) power)&0x00FFFFFF)|opacity;

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
