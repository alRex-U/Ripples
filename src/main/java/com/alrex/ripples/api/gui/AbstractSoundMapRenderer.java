package com.alrex.ripples.api.gui;

import com.alrex.ripples.api.audio.SoundMapSource;
import com.alrex.ripples.config.RipplesConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import java.util.List;

public abstract class AbstractSoundMapRenderer {
    public abstract void render(ForgeGui forgeGui, GuiGraphics guiGraphics, List<SoundMapSource> soundSources, float partialTick, int width, int height);

    public ColorPallet getColorPallet(){
        return RipplesConfig.getColorPallet();
    }
}
