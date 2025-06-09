package com.alrex.ripples.render.hud.soundmap;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.api.audio.SoundMapSource;
import com.alrex.ripples.api.gui.AbstractSoundMapRenderer;
import com.alrex.ripples.render.RenderUtil;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.joml.Matrix4f;

import java.util.List;

public class RadarSoundMap extends AbstractSoundMapRenderer {
    public static final ResourceLocation SOUND_MAP_ID=new ResourceLocation(Ripples.MOD_ID,"radar");
    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, List<SoundMapSource> soundSources, float partialTick, int width, int height) {
        float offsetX=width/2f;
        float offsetY=height/2f;
        float radius=Math.min(width/7f,height/4f);

        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        var renderType= RenderUtil.RenderTypes.guiLineStrip();
        VertexConsumer vertexconsumer = guiGraphics.bufferSource().getBuffer(renderType);
        double baseAngle=2.*Math.PI/(256);
        float a=1;
        var pallet=getColorPallet();
        for (int i=0;i<=256;i++){
            int color=pallet.getColor(0);
            float r = (float) FastColor.ARGB32.red(color) / 255.0F;
            float g = (float) FastColor.ARGB32.green(color) / 255.0F;
            float b = (float) FastColor.ARGB32.blue(color) / 255.0F;

            double angle=-Math.PI/2. + baseAngle*i;
            float x= offsetX+(float) (radius*(Math.cos(angle)));
            float y= offsetY-(float) (radius*(Math.sin(angle)));
            vertexconsumer.vertex(matrix4f,x,y,-1).color(r,g,b,a).endVertex();
        }
        guiGraphics.flush();

        for (var source:soundSources){
            float pressure=source.soundPressure();
        }
    }
}
