package com.alrex.ripples.render.hud;

import com.alrex.ripples.resources.MusicInfo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class MusicInfoToastHUD implements IGuiOverlay {
    private static final int MARGIN=5;
    @Nullable
    private MusicInfo musicInfo;
    @Nullable
    private String composerString;
    @Nullable
    private String musicNameString;
    private int musicInfoShowingTickCount;

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partial, int width, int height) {
        if (musicInfoShowingTickCount >= 100 || musicInfo==null || musicNameString==null || composerString==null)return;
        final var composerTextScale=0.8f;
        var font=forgeGui.getFont();
        var componentHeight=font.lineHeight*(1+composerTextScale)+3;
        var composerWidth=(int)(font.width(composerString)*composerTextScale);
        var musicWidth=font.width(musicNameString);
        var componentWidth=MARGIN*2+Math.max(composerWidth,musicWidth);

        float showingTickCount=musicInfoShowingTickCount+partial;
        float offsetX=width-componentWidth;
        int offsetY= (int) (height-10-componentHeight);
        if (showingTickCount<10){
            float phase=showingTickCount/10f;
            offsetX+=componentWidth*Mth.square(1-phase);
        }else if (showingTickCount>90){
            float phase=(showingTickCount-90)/10f;
            offsetX+=componentWidth*Mth.square(phase);
        }

        guiGraphics.drawString(font,musicNameString,(int)(offsetX+componentWidth-MARGIN-musicWidth),offsetY,0xFFFFFFFF);
        guiGraphics.hLine((int) offsetX, (int) (offsetX+componentWidth),offsetY+font.lineHeight+1,0xFFFFFFFF);
        guiGraphics.pose().pushPose();
        {
            guiGraphics.pose().translate((int)(offsetX+componentWidth-MARGIN-composerWidth),offsetY+font.lineHeight+4,0);
            guiGraphics.pose().scale(composerTextScale,composerTextScale,composerTextScale);
            guiGraphics.drawString(font,composerString,0,0,0xFFFFFFFF);
        }
        guiGraphics.pose().popPose();
    }

    public void tick(){
        if (musicInfoShowingTickCount < 200)musicInfoShowingTickCount++;
    }

    public void notifyStartMusic(MusicInfo info){
        musicInfoShowingTickCount=0;
        this.musicInfo=info;
        composerString="by "+ info.composer();
        musicNameString="♪ "+info.title();
    }
}
