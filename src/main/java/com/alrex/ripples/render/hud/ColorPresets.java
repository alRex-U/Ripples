package com.alrex.ripples.render.hud;

import com.alrex.ripples.api.gui.ColorPallet;

public enum ColorPresets {
    SPRING("ripples.color_presets.spring",new int[]{0xFFFFFF,0xE3C2D5,0xD25298,0xB92053});
    private final String translationKey;
    private final int[] colors;
    ColorPresets(String translationKey,int[] colors){
        this.translationKey=translationKey;
        this.colors=colors;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public ColorPallet newPallet(){
        return new ColorPallet(colors);
    }
}
