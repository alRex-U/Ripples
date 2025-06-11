package com.alrex.ripples.render.hud.soundmap;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.api.audio.SoundMapSource;
import com.alrex.ripples.api.gui.AbstractSoundMapRenderer;
import com.alrex.ripples.render.RenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import java.util.List;

public class HorizontalSoundMap extends AbstractSoundMapRenderer {
    public static final ResourceLocation SOUND_MAP_ID=new ResourceLocation(Ripples.MOD_ID,"horizontal");
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, List<SoundMapSource> soundSources, float partialTick, int width, int height) {
        int componentWidth=180;
        int offsetX=width/2;
        int offsetY=10;
        var pallet=getColorPallet();

        int opacityInt=getOpacityInt();
        var opacity=(float) getOpacity();
        int unitLineWidth=componentWidth/20;
        var renderType= RenderType.gui();
        for (int i=0;i<10;i++){
            float opacity1=opacity*(1f-i/10f);
            float opacity2=opacity*(1f-(i+1)/10f);
            float x1=unitLineWidth*i;
            float x2=unitLineWidth*(i+1);
            float y1=offsetY;
            float y2=offsetY+1.2f;
            var vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
            vertexconsumer.vertex(offsetX+x1,y1,-1).color(1f,1f,1f,opacity1).endVertex();
            vertexconsumer.vertex(offsetX+x1,y2,-1).color(1f,1f,1f,opacity1).endVertex();
            vertexconsumer.vertex(offsetX+x2,y2,-1).color(1f,1f,1f,opacity2).endVertex();
            vertexconsumer.vertex(offsetX+x2,y1,-1).color(1f,1f,1f,opacity2).endVertex();
            guiGraphics.flush();
            vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
            vertexconsumer.vertex(offsetX-x1,y1,-1).color(1f,1f,1f,opacity1).endVertex();
            vertexconsumer.vertex(offsetX-x2,y1,-1).color(1f,1f,1f,opacity2).endVertex();
            vertexconsumer.vertex(offsetX-x2,y2,-1).color(1f,1f,1f,opacity2).endVertex();
            vertexconsumer.vertex(offsetX-x1,y2,-1).color(1f,1f,1f,opacity1).endVertex();
            guiGraphics.flush();
        }

        float gain=50f;
        float size=9f;
        for (var source:soundSources){
            var relativeAngle=source.relativeAngle();
            if (relativeAngle==null)continue;
            float distanceScaleFromCenter=Mth.clamp((relativeAngle.distanceRadianFromPointOfInterest()/Mth.PI)*Mth.sin(relativeAngle.angleRadian()),-1f,1f);
            float x= offsetX-(componentWidth/2f)*distanceScaleFromCenter;
            float y=offsetY+0.6f;

            float factor=(float) getPower(source.soundPressure(),gain);
            float soundSize= size*factor;
            int baseColor=pallet.getColor(factor)&0xFFFFFF;
            int centerColor=baseColor| ((int) (opacityInt*(1f-Mth.abs(distanceScaleFromCenter)*0.5f)) << 24);
            RenderUtil.drawCircle(
                    guiGraphics,
                    x,y,-1,
                    soundSize/2f,
                    centerColor,
                    baseColor
            );
        }
    }
    private double getPower(float pressure,float gain){
        return Math.min(Math.log(pressure*gain+1),1f);
    }
}
