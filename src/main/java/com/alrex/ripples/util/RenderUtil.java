package com.alrex.ripples.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RenderUtil {
    private static class RenderTypes{
       private static final RenderType LINES= RenderType.create(
               "gui_lines",
               DefaultVertexFormat.POSITION_COLOR,
               VertexFormat.Mode.LINE_STRIP, 256,
               false,false,
               RenderType.CompositeState.builder()
                       .setShaderState(RenderType.RENDERTYPE_GUI_SHADER)
                       .setDepthTestState(RenderType.LEQUAL_DEPTH_TEST)
                       .createCompositeState(false)
       );

        public static RenderType lines() {
            return LINES;
        }
    }
    public static void fillWithFloatPos(GuiGraphics graphics, float x, float y, float x2, float y2, float z, int color){
        Matrix4f matrix4f = graphics.pose().last().pose();
        float temp;
        if (x < x2) {
            temp = x;
            x = x2;
            x2 = temp;
        }

        if (y < y2) {
            temp = y;
            y = y2;
            y2 = temp;
        }

        var renderType=RenderType.gui();
        float a = (float) FastColor.ARGB32.alpha(color) / 255.0F;
        float r = (float) FastColor.ARGB32.red(color) / 255.0F;
        float g = (float) FastColor.ARGB32.green(color) / 255.0F;
        float b = (float) FastColor.ARGB32.blue(color) / 255.0F;
        VertexConsumer vertexconsumer = graphics.bufferSource().getBuffer(renderType);
        vertexconsumer.vertex(matrix4f, x, y, z).color(r, g, b, a).endVertex();
        vertexconsumer.vertex(matrix4f, x, y2, z).color(r, g, b, a).endVertex();
        vertexconsumer.vertex(matrix4f, x2,y2, z).color(r, g, b, a).endVertex();
        vertexconsumer.vertex(matrix4f, x2,y, z).color(r, g, b, a).endVertex();
        graphics.flush();
    }
    public static void drawPolyline(GuiGraphics graphics, float[] x, float[] y,float z, int color){
        int count=Math.min(x.length,y.length);

        Matrix4f matrix4f = graphics.pose().last().pose();
        var renderType=RenderTypes.lines();
        float a = (float) FastColor.ARGB32.alpha(color) / 255.0F;
        float r = (float) FastColor.ARGB32.red(color) / 255.0F;
        float g = (float) FastColor.ARGB32.green(color) / 255.0F;
        float b = (float) FastColor.ARGB32.blue(color) / 255.0F;
        VertexConsumer vertexconsumer = graphics.bufferSource().getBuffer(renderType);
        for(var i=0;i<count;i++){
            vertexconsumer.vertex(matrix4f,x[i],y[i],z).color(r,g,b,a).endVertex();
        }
        graphics.flush();
    }
}
