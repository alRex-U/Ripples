package com.alrex.ripples.render.hud.soundmap;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.api.audio.SoundMapSource;
import com.alrex.ripples.api.gui.AbstractSoundMapRenderer;
import com.alrex.ripples.render.RenderUtil;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.joml.Matrix4f;

import java.util.List;

public class RadarSoundMap extends AbstractSoundMapRenderer {
    public static final ResourceLocation SOUND_MAP_ID=new ResourceLocation(Ripples.MOD_ID,"radar");
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, List<SoundMapSource> soundSources, float partialTick, int width, int height) {
        float offsetX=width/2f;
        float offsetY=height/2f;
        float radius=50;
        float outerRadius=radius+20;

        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        var renderType= RenderUtil.RenderTypes.guiLineStrip();
        double baseAngle=2.*Math.PI/(256);
        var opacity=(float)getOpacity();
        var opacityI=getOpacityInt() << 24;
        var pallet=getColorPallet();
        VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
        var ringOpacity=opacity*0.85f;
        for (int i=0;i<=256;i++){
            double angle=-Math.PI/2. + baseAngle*i;
            float x= offsetX+(float) (radius*(Math.cos(angle)));
            float y= offsetY+(float) (radius*(Math.sin(angle)));
            vertexconsumer.vertex(matrix4f,x,y,-1).color(1f,1f,1f, ringOpacity).endVertex();
        }
        ringOpacity=opacity*0.7f;
        guiGraphics.flush();
        vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
        for (int i=0;i<=256;i++){
            double angle=-Math.PI/2. + baseAngle*i;
            float x= offsetX+(float) (outerRadius*(Math.cos(angle)));
            float y= offsetY+(float) (outerRadius*(Math.sin(angle)));
            vertexconsumer.vertex(matrix4f,x,y,-1).color(1f,1f,1f, ringOpacity).endVertex();
        }
        guiGraphics.flush();

        float size=7.5f;
        float gain=100f;
        for (var source:soundSources){
            if (source.relativeAngle()==null)continue;
            float pressure=source.soundPressure();
            float sourceRadius=getSourceRadius(source,radius,outerRadius);

            float angle=source.relativeAngle().angleRadian();
            float x=offsetX-sourceRadius*Mth.sin(angle);
            float y=offsetY-sourceRadius*Mth.cos(angle);

            float factor=(float) getPower(pressure,gain);
            float soundSize= size*factor;
            int color=(pallet.getColor(factor)&0xFFFFFF)| ((int) Mth.clamp(opacity*0.75,0.,1.) << 24);
            int centerColor=(pallet.getColor(factor)&0xFFFFFF)| opacityI;

            RenderUtil.fillWithFloatPos(
                    guiGraphics,
                    x-soundSize/2f,
                    y-soundSize/2f,
                    x+soundSize/2f,
                    y+soundSize/2f,
                    -1,
                    color
            );
            RenderUtil.fillWithFloatPos(
                    guiGraphics,
                    x-soundSize/4f,
                    y-soundSize/4f,
                    x+soundSize/4f,
                    y+soundSize/4f,
                    -1,
                    centerColor
            );
        }
    }

    private double getPower(float pressure,float gain){
        return Math.min(Math.log(pressure*gain+1),1f);
    }

    private float getSourceRadius(SoundMapSource source,float innerRadius,float outerRadius){
        if (source.relativeAngle()==null)return 0;
        float distanceAngle=source.relativeAngle().distanceRadianFromPointOfInterest();
        if (distanceAngle< Mth.HALF_PI){
            return innerRadius*(distanceAngle/Mth.HALF_PI);
        }else {
            return Mth.lerp(distanceAngle/Mth.HALF_PI-1f,innerRadius,outerRadius);
        }
    }
}
