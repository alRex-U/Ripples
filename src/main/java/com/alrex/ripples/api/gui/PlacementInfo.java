package com.alrex.ripples.api.gui;

public record PlacementInfo(boolean isDefault, float x, float y) {
    public static PlacementInfo DEFAULT=new PlacementInfo(true,0,0);
}
