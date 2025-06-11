package com.alrex.ripples.render.hud.soundmap;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.api.audio.RelativeAngleInFOV;
import com.alrex.ripples.api.audio.SoundMapSource;
import com.alrex.ripples.api.gui.AbstractSoundMapRenderer;
import com.alrex.ripples.render.Colors;
import com.alrex.ripples.render.RenderUtil;
import com.alrex.ripples.util.MathUtil;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.joml.Matrix4f;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CircleSoundMap extends AbstractSoundMapRenderer {
    public static final ResourceLocation SOUND_MAP_ID=new ResourceLocation(Ripples.MOD_ID,"circle");
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, List<SoundMapSource> soundSources, float partialTick, int width, int height) {
        final int divideCount=1024;
        float[] values=new float[divideCount];
        float radiusPower=0;
        for (var source:soundSources){
            var relativeAngle=source.relativeAngle();
            if (relativeAngle==null){
                continue;
            }
            float peakFactor= (float) Math.sqrt(relativeAngle.distanceRadianFromPointOfInterest()/Math.PI);
            float radiusFactor=1-peakFactor;

            float soundPower=source.soundPressure();
            float peakPower=peakFactor*soundPower;
            int centerIndex= Mth.clamp((int)(values.length*(Math.PI+ MathUtil.normalizeRadian(source.relativeAngle().angleRadian()))/(2d*Math.PI)),0,values.length-1);
            values[centerIndex]+=peakPower;
            for (var i=1;i<values.length/8;i++){
                float value=peakPower/(i*i+1);
                if (value < 0.0001)break;
                values[(centerIndex+i)%values.length]+=value;
                values[(values.length+centerIndex-i)%values.length]+=value;
            }
            radiusPower+=radiusFactor*soundPower;
        }
        float radiusGain=0.1f;
        radiusPower*=radiusGain;

        float offsetX=width/2f;
        float offsetY=height/2f;
        float radius=50f;
        double powerScale=radius*0.8;
        float gain=50f;
        var pallet=getColorPallet();

        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        var renderType= RenderUtil.RenderTypes.guiLineStrip();
        VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
        double baseAngle=2.*Math.PI/(values.length-1);
        float a = (float) getOpacity();
        for(var i=0;i<values.length;i++){
            double power=getPower(radiusPower,gain)+getPower(values[i],gain);

            int color=pallet.getColor((float) power);
            var colorF= Colors.ARGB_F.getFromFastColorRGB(color);

            double angle=-Math.PI/2. + baseAngle*i;
            float x= getX(offsetX,radius,power,powerScale,angle);
            float y= getY(offsetY,radius,power,powerScale,angle);
            vertexconsumer.vertex(matrix4f,x,y,-1).color(colorF.r(),colorF.g(),colorF.b(),a).endVertex();
        }
        guiGraphics.flush();
    }
    private double getPower(float power,float gain){
        return Math.min(Math.log(power*gain+1),1f);
    }
    private float getX(float offsetX,double radius,double power,double powerScale,double angle){
        return offsetX+(float) ((radius+power*powerScale)*(Math.cos(angle)));
    }
    private float getY(float offsetY,double radius,double power,double powerScale,double angle){
        return offsetY-(float) ((radius+power*powerScale)*(Math.sin(angle)));
    }
}
