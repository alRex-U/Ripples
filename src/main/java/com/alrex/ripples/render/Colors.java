package com.alrex.ripples.render;

import net.minecraft.util.FastColor;

public class Colors {
    public record ARGB_F(float a,float r,float g, float b){
        public static ARGB_F getFromFastColorARGB(int color){
            float a = (float) FastColor.ARGB32.alpha(color) / 255.0F;
            float r = (float) FastColor.ARGB32.red(color) / 255.0F;
            float g = (float) FastColor.ARGB32.green(color) / 255.0F;
            float b = (float) FastColor.ARGB32.blue(color) / 255.0F;
            return new ARGB_F(a,r,g,b);
        }
        public static ARGB_F getFromFastColorRGB(int color){
            float r = (float) FastColor.ARGB32.red(color) / 255.0F;
            float g = (float) FastColor.ARGB32.green(color) / 255.0F;
            float b = (float) FastColor.ARGB32.blue(color) / 255.0F;
            return new ARGB_F(1f,r,g,b);
        }
    }
}
