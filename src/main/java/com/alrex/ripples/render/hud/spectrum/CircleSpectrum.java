package com.alrex.ripples.render.hud.spectrum;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.api.gui.AbstractSpectrumRenderer;
import com.alrex.ripples.render.Colors;
import com.alrex.ripples.render.RenderUtil;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class CircleSpectrum extends AbstractSpectrumRenderer {
    public static final ResourceLocation SPECTRUM_ID=new ResourceLocation(Ripples.MOD_ID,"circle");
    public static final ResourceLocation SPECTRUM_ID_MIRRORED=new ResourceLocation(Ripples.MOD_ID,"mirrored_circle");
    private final boolean mirrored;

    public CircleSpectrum(boolean mirrored){
        this.mirrored=mirrored;
    }

    public static CircleSpectrum normal(){
        return new CircleSpectrum(false);
    }
    public static CircleSpectrum mirrored(){
        return new CircleSpectrum(true);
    }

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float[] ft, float partialTick, int width, int height) {
        float offsetX=width/2f;
        float offsetY=height/2f;
        double radius=50;
        double powerRadius=radius*0.8;
        double gain=420f;
        int loopCount=mirrored ? 2*ft.length : ft.length;
        double startAngle=-Math.PI/2.;
        double baseAngle=2.*Math.PI/(loopCount-1);

        var pallet=getColorPallet();
        switch (getSpectrumStyle()){
            case DEFAULT,LINES -> {
                Matrix4f matrix4f = guiGraphics.pose().last().pose();
                var renderType= RenderUtil.RenderTypes.guiLineStrip();
                VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
                float a = (float) getOpacity();
                for(var i=0;i<loopCount;i++){
                    int index=i;
                    if (i>=ft.length) index=2* ft.length-1-i;
                    double power=getPower(ft[index],gain);

                    int color=pallet.getColor((float) power);
                    var colorF= Colors.ARGB_F.getFromFastColorRGB(color);

                    double angle=startAngle + baseAngle*i;
                    float x= getX(offsetX,radius,power,powerRadius,angle);
                    float y= getY(offsetY,radius,power,powerRadius,angle);
                    vertexconsumer.vertex(matrix4f,x,y,-1).color(colorF.r(),colorF.g(),colorF.b(),a).endVertex();
                }
                guiGraphics.flush();
            }
            case LINES_FILL ->{
                Matrix4f matrix4f = guiGraphics.pose().last().pose();
                var renderType= RenderUtil.RenderTypes.guiTriangleFan();
                VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
                float a = (float) getOpacity();
                {
                    var centerColor=Colors.ARGB_F.getFromFastColorRGB(pallet.getColor(0));
                    vertexconsumer.vertex(matrix4f,offsetX,offsetY,-1).color(centerColor.r(),centerColor.g(),centerColor.b(),a).endVertex();
                }
                for(var i=0;i<loopCount;i++){
                    int index=i;
                    if (i>=ft.length) index=2* ft.length-1-i;
                    double power=getPower(ft[index],gain);

                    int color=pallet.getColor((float) power);
                    var colorF= Colors.ARGB_F.getFromFastColorRGB(color);

                    double angle=startAngle + baseAngle*i;
                    float x= getX(offsetX,radius,power,powerRadius,angle);
                    float y= getY(offsetY,radius,power,powerRadius,angle);
                    vertexconsumer.vertex(matrix4f,x,y,-1).color(colorF.r(),colorF.g(),colorF.b(),a).endVertex();
                }
                guiGraphics.flush();
            }
            case BLOCKS -> {
                int opacity=getOpacityInt()<<24;
                var xArray=new float[4];
                var yArray=new float[4];
                for(var i=0;i<loopCount;i++){
                    int index=i;
                    if (i>=ft.length) index=2* ft.length-1-i;
                    double power=getPower(ft[index],gain);

                    int colorSize=pallet.getNumberOfColors();
                    int division= Mth.ceil(colorSize*power);
                    if (division <=0) division=1;

                    int color=(pallet.getColor((float) power)&0x00FFFFFF)|opacity;
                    var renderType= RenderUtil.RenderTypes.guiTriangleFan();

                    double angle=startAngle + baseAngle*i;
                    float baseX= getX(offsetX,radius,0,0,angle);
                    float baseY= getY(offsetY,radius,0,0,angle);
                    float xDifference1=0.5f*(getX(offsetX,radius,0,0,angle-baseAngle)-baseX);
                    float xDifference2=0.5f*(getX(offsetX,radius,0,0,angle+baseAngle)-baseX);
                    float yDifference1=0.5f*(getY(offsetY,radius,0,0,angle-baseAngle)-baseY);
                    float yDifference2=0.5f*(getY(offsetY,radius,0,0,angle+baseAngle)-baseY);
                    int color1,color2;
                    for (var j=0;j<division;j++){
                        color1=(pallet.getColor(j)&0xFFFFFF) | opacity;

                        xArray[0]=baseX+xDifference1;
                        yArray[0]=baseY+yDifference1;
                        xArray[3]=baseX+xDifference2;
                        yArray[3]=baseY+yDifference2;
                        if (j<division-1) {
                            color2=(pallet.getColor(j+1)&0xFFFFFF) | opacity;
                            baseX = getX(offsetX, radius, (j + 1.)/(colorSize), powerRadius, angle);
                            baseY = getY(offsetY, radius, (j + 1.)/(colorSize), powerRadius, angle);
                        }else {
                            color2=color;
                            baseX = getX(offsetX, radius, power, powerRadius, angle);
                            baseY = getY(offsetY, radius, power, powerRadius, angle);
                        }
                        xArray[1]=baseX+xDifference1;
                        yArray[1]=baseY+yDifference1;
                        xArray[2]=baseX+xDifference2;
                        yArray[2]=baseY+yDifference2;

                        Matrix4f matrix4f = guiGraphics.pose().last().pose();

                        VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
                        vertexconsumer.vertex(matrix4f,xArray[0],yArray[0],-1).color(color1).endVertex();
                        vertexconsumer.vertex(matrix4f,xArray[1],yArray[1],-1).color(color2).endVertex();
                        vertexconsumer.vertex(matrix4f,xArray[2],yArray[2],-1).color(color2).endVertex();
                        vertexconsumer.vertex(matrix4f,xArray[3],yArray[3],-1).color(color1).endVertex();
                        guiGraphics.flush();
                    }
                }
            }
            case POINTS -> {
                int opacity=getOpacityInt()<<24;
                for(var i=0;i<loopCount;i++){
                    int index=i;
                    if (i>=ft.length) index=2* ft.length-1-i;
                    double power=getPower(ft[index],gain);

                    int color=(pallet.getColor((float) power)&0x00FFFFFF)|opacity;

                    double angle=startAngle + baseAngle*i;
                    float x= getX(offsetX,radius,power,powerRadius,angle);
                    float y= getY(offsetY,radius,power,powerRadius,angle);
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
    private double getPower(float ft,double gain){
        return Math.min(Math.log10(ft*gain+1),1f);
    }
    private float getX(float offsetX,double radius,double power,double powerScale,double angle){
        return offsetX+(float) ((radius+power*powerScale)*(Math.cos(angle)));
    }
    private float getY(float offsetY,double radius,double power,double powerScale,double angle){
        return offsetY+(float) ((radius+power*powerScale)*(Math.sin(angle)));
    }
}
