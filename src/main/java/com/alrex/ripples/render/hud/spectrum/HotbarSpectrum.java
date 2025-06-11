package com.alrex.ripples.render.hud.spectrum;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.api.gui.AbstractSpectrumRenderer;
import com.alrex.ripples.render.Colors;
import com.alrex.ripples.render.RenderUtil;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class HotbarSpectrum extends AbstractSpectrumRenderer {
    public static final ResourceLocation SPECTRUM_ID=new ResourceLocation(Ripples.MOD_ID,"hotbar");
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float[] ft, float partialTick, int width, int height) {
        int baseY=height-22;

        int hotBarWidth=182;
        float baseX=(width-hotBarWidth)/2f;
        float barWidth=hotBarWidth/(float)ft.length;
        double barHeight=20;
        double gain=420.;
        int opacity=getOpacityInt();
        var pallet=getColorPallet();

        switch (getSpectrumStyle()){
            case DEFAULT, BLOCKS -> {
                opacity=opacity<<24;
                int colorSize=pallet.getNumberOfColors();
                double divisionHeight=barHeight/colorSize;
                for(var i=0;i<ft.length;i++){
                    double power=getPower(ft[i],gain);
                    int color=(pallet.getColor((float) power)&0x00FFFFFF)|opacity;
                    float y=getY(power,baseY,barHeight);
                    int division=Mth.ceil(colorSize*power);
                    if (division <=0) division=1;
                    var renderType= RenderUtil.RenderTypes.guiQuads();
                    for (int j=0;j<division-1;j++){
                        float x1=baseX+barWidth*i;
                        float x2= baseX+barWidth*(i+1);
                        float y1=(float) (baseY-j*divisionHeight);
                        float y2=(float) (baseY-(j+1)*divisionHeight);
                        int color1=pallet.getColor(j) | opacity;
                        int color2=pallet.getColor(j+1)|opacity;
                        var vertexConsumer=guiGraphics.bufferSource().getBuffer(renderType);
                        vertexConsumer.vertex(x1,y1,-1).color(color1).endVertex();
                        vertexConsumer.vertex(x2,y1,-1).color(color1).endVertex();
                        vertexConsumer.vertex(x2,y2,-1).color(color2).endVertex();
                        vertexConsumer.vertex(x1,y2,-1).color(color2).endVertex();
                        guiGraphics.flush();
                    }
                    RenderUtil.fillWithFloatPos(
                            guiGraphics,
                            baseX+barWidth*i,
                            (float) (baseY-(division-1)*divisionHeight),
                            baseX+barWidth*(i+1),
                            y,
                            -1,
                            color
                    );
                }
            }
            case LINES_FILL -> {
                opacity=opacity<<24;
                for(var i=0;i<ft.length;i++){
                    double power=getPower(ft[i],gain);
                    int color=(pallet.getColor((float) power)&0x00FFFFFF)|opacity;
                    float y=getY(power,baseY,barHeight);
                    RenderUtil.fillWithFloatPos(
                            guiGraphics,
                            baseX+barWidth*i,
                            baseY,
                            baseX+barWidth*(i+1),
                            y,
                            -1,
                            color
                    );
                }
            }
            case LINES -> {
                Matrix4f matrix4f = guiGraphics.pose().last().pose();
                var renderType= RenderUtil.RenderTypes.guiLineStrip();
                VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
                float a=opacity/255f;
                for(var i=0;i< ft.length;i++){
                    double power=getPower(ft[i],gain);
                    int color=pallet.getColor((float) power);
                    var colorF= Colors.ARGB_F.getFromFastColorRGB(color);
                    float x= baseX+barWidth*i;
                    float y= getY(power,baseY,barHeight);
                    vertexconsumer.vertex(matrix4f,x,y,-1).color(colorF.r(),colorF.g(),colorF.b(),a).endVertex();
                }
                guiGraphics.flush();
            }
            case POINTS -> {
                opacity=opacity<<24;
                for(var i=0;i<ft.length;i++){
                    double power=getPower(ft[i],gain);
                    int color=(pallet.getColor((float) power)&0x00FFFFFF)|opacity;
                    float y= getY(power,baseY,barHeight);
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
    private double getPower(float ft,double gain){
        return Math.min(Math.log10(ft*gain+1),1f);
    }
    private float getY(double power, int baseY,double barHeight){
        return (float) Mth.lerp(power,baseY,baseY-barHeight);
    }
}
