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

public class HotbarSpectrum extends AbstractSpectrumRenderer {
    public static final ResourceLocation SPECTRUM_ID=new ResourceLocation(Ripples.MOD_ID,"hotbar");
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float[] ft, float partialTick, int width, int height) {
        int baseY=height-22;

        int hotBarWidth=20*9;
        float baseX=(width-hotBarWidth)/2f;
        float barWidth=hotBarWidth/(float)ft.length;
        int opacity=getOpacityInt();
        var pallet=getColorPallet();
        switch (getSpectrumStyle()){
            case DEFAULT, BLOCKS, LINES_FILL -> {
                opacity=opacity<<24;
                for(var i=0;i<ft.length;i++){
                    double power=Math.min(Math.log(ft[i]*100f+1),1f);
                    int color=(pallet.getColor((float) power)&0x00FFFFFF)|opacity;
                    RenderUtil.fillWithFloatPos(
                            guiGraphics,
                            baseX+barWidth*i,
                            (float) baseY,
                            baseX+barWidth*(i+1),
                            (float) Mth.lerp(power,baseY,baseY-20),
                            -1,
                            color
                    );
                }
            }
            case LINES -> {
                Matrix4f matrix4f = guiGraphics.pose().last().pose();
                var renderType= RenderUtil.RenderTypes.lines();
                VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
                float a=opacity/255f;
                for(var i=0;i< ft.length;i++){
                    double power=Math.min(Math.log(ft[i]*100f+1),1f);
                    int color=pallet.getColor((float) power);
                    float r = (float) FastColor.ARGB32.red(color) / 255.0F;
                    float g = (float) FastColor.ARGB32.green(color) / 255.0F;
                    float b = (float) FastColor.ARGB32.blue(color) / 255.0F;
                    float x= baseX+barWidth*i;
                    float y= (float) Mth.lerp(power,baseY,baseY-20);
                    vertexconsumer.vertex(matrix4f,x,y,-1).color(r,g,b,a).endVertex();
                }
                guiGraphics.flush();
            }
            case POINTS -> {
                opacity=opacity<<24;
                for(var i=0;i<ft.length;i++){
                    double power=Math.min(Math.log(ft[i]*100f+1),1f);
                    int color=(pallet.getColor((float) power)&0x00FFFFFF)|opacity;
                    float y= (float) Mth.lerp(power,baseY,baseY-20);
                    RenderUtil.fillWithFloatPos(
                            guiGraphics,
                            baseX+barWidth*(i-1),
                            y+barWidth*3,
                            baseX+barWidth*(i+2),
                            y,
                            -1,
                            color
                    );
                }
            }
        }

    }
}
