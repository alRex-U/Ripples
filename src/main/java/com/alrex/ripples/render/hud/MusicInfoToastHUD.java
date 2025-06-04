package com.alrex.ripples.render.hud;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import javax.annotation.Nullable;

public class MusicInfoToastHUD implements IGuiOverlay {
    @Nullable
    private String musicName;
    private int musicInfoShowingTickCount;

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float v, int i, int i1) {

    }

    public void tick(){
        musicInfoShowingTickCount++;
    }

    public void notifyStartMusic(ResourceLocation location){
        musicInfoShowingTickCount++;
    }
}
