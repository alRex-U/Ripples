package com.alrex.ripples.render.hud.soundmap;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.api.audio.SoundMapSource;
import com.alrex.ripples.api.gui.AbstractSoundMapRenderer;
import com.alrex.ripples.render.RenderUtil;
import com.alrex.ripples.util.MathUtil;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.joml.Matrix4f;

import java.util.List;

public class CircleSoundMap extends AbstractSoundMapRenderer {
    public static final ResourceLocation SOUND_MAP_ID=new ResourceLocation(Ripples.MOD_ID,"circle");
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, List<SoundMapSource> soundSources, float partialTick, int width, int height) {
        final int divideCount=1024;
        float[] values=new float[divideCount];
        float radiusPower=0;
        for (var source:soundSources){
            float peakFactor= (float) Math.sqrt(source.relativeAngle().distanceRadianFromPointOfInterest()/Math.PI);
            float radiusFactor=1-peakFactor;

            float soundPower=source.soundPressure();
            float peak=peakFactor*soundPower;
            int centerIndex= Mth.clamp((int)(values.length*(Math.PI+ MathUtil.normalizeRadian(source.relativeAngle().angleRadian()))/(2d*Math.PI)),0,values.length-1);
            values[centerIndex]+=peak;
            for (var i=1;i<values.length/8;i++){
                float value=peak/(i*i+1);
                if (value < 0.0001)break;
                values[(centerIndex+i)%values.length]+=value;
                values[(values.length+centerIndex-i)%values.length]+=value;
            }
            radiusPower+=radiusFactor*soundPower/3f;
        }

        float offsetX=width/2f;
        float offsetY=height/2f;
        float radius=Math.min(width/7f,height/4f);
        var pallet=getColorPallet();

        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        var renderType= RenderUtil.RenderTypes.guiLineStrip();
        VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
        double baseAngle=2.*Math.PI/(values.length);
        //float a = (float) getOpacity();
        float a=1;
        for(var i=0;i<values.length;i++){
            double power=Mth.lerp(Math.min(Math.log((radiusPower+values[i])*100f+1),1f),0,radius/3f);

            int color=pallet.getColor((float) power);
            float r = (float) FastColor.ARGB32.red(color) / 255.0F;
            float g = (float) FastColor.ARGB32.green(color) / 255.0F;
            float b = (float) FastColor.ARGB32.blue(color) / 255.0F;

            double angle=-Math.PI/2. + baseAngle*i;
            float x= offsetX+(float) ((radius+power)*(Math.cos(angle)));
            float y= offsetY-(float) ((radius+power)*(Math.sin(angle)));
            vertexconsumer.vertex(matrix4f,x,y,-1).color(r,g,b,a).endVertex();
        }
        guiGraphics.flush();
    }
}
