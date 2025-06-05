package com.alrex.ripples.render.hud;

import net.minecraft.util.FastColor;

import java.util.Arrays;

public class ColorPallet {
    private static final int DEFAULT_COLOR=0xFFFFFF;

    public ColorPallet(){
        this.colors= new int[0];
    }
    public ColorPallet(int... colors){
        this.colors=colors;
    }
    private final int[] colors;
    public int getNumberOfColors(){
        if (colors.length==0)return 1;
        return colors.length;
    }
    public int getColor(int index){
        if (index<0 || index>=colors.length)return DEFAULT_COLOR;
        return colors[index];
    }
    public int getColor(float factor){
        if (factor<0f||colors.length<=1)return getColor(0);
        if (factor>1f)return getColor(getNumberOfColors()-1);
        float indexFactor=factor*(getNumberOfColors()-1);
        int color1Index=(int)indexFactor;
        if (color1Index >= colors.length-1)return colors[colors.length-1];
        int color2Index=color1Index+1;
        float mixFactor=indexFactor-color1Index;
        int color1=getColor(color1Index),color2=getColor(color2Index);
        return mix(color1,color2,mixFactor);
    }
    private static int mix(int color1,int color2,float factor){
        return FastColor.ARGB32.lerp(factor,color1,color2);
    }
}
