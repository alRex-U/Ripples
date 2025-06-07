package com.alrex.ripples.render.hud;

import com.alrex.ripples.resources.MusicInfo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import javax.annotation.Nullable;

public class MusicInfoToastHUD implements IGuiOverlay {
    @Nullable
    private MusicInfo musicInfo;
    @Nullable
    private String composerString;
    private int musicInfoShowingTickCount;

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partial, int width, int height) {
        if (musicInfoShowingTickCount >= 100 || musicInfo==null || composerString==null)return;
        var font=forgeGui.getFont();
        var componentHeight=font.lineHeight*2+3;
        var composerWidth=font.width(composerString);
        var musicWidth=font.width(musicInfo.musicName());
        var componentWidth=10+Math.max(composerWidth,musicWidth);

        float showingTickCount=musicInfoShowingTickCount+partial;
        float offsetX=width-componentWidth;
        int offsetY=height-10-componentHeight;
        if (showingTickCount<10){
            float phase=showingTickCount/10f;
            offsetX+=componentWidth*Mth.square(1-phase);
        }else if (showingTickCount>90){
            float phase=(showingTickCount-90)/10f;
            offsetX+=componentWidth*Mth.square(phase);
        }

        guiGraphics.drawString(font,musicInfo.musicName(),(int)(offsetX+componentWidth-5-musicWidth),offsetY,0xFFFFFFFF);
        guiGraphics.hLine((int) offsetX, (int) (offsetX+componentWidth),offsetY+font.lineHeight+1,0xFFFFFFFF);
        guiGraphics.drawString(font,composerString,(int)(offsetX+componentWidth-5-composerWidth),offsetY+font.lineHeight+4,0xFFFFFFFF);
    }

    public void tick(){
        if (musicInfoShowingTickCount < 200)musicInfoShowingTickCount++;
    }

    public void notifyStartMusic(MusicInfo info){
        musicInfoShowingTickCount=0;
        this.musicInfo=info;
        composerString="by "+ info.composer();
    }
}
