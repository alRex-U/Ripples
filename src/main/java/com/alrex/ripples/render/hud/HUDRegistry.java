package com.alrex.ripples.render.hud;

import com.alrex.ripples.api.gui.AbstractSpectrumRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HUDRegistry {
    private static final SpectrumRendererHUD spectrumRenderer=new SpectrumRendererHUD();
    private static final MusicInfoToastHUD musicInfoToast=new MusicInfoToastHUD();

    @SubscribeEvent
    public static void onSetup(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("hud.spectrum_host", spectrumRenderer);
        event.registerAboveAll("hud.music_info_toast", musicInfoToast);
    }

    public static void tick(){
        musicInfoToast.tick();
    }

    public static void notifyStartMusic(ResourceLocation location){

    }

    public static void setSpectrum(AbstractSpectrumRenderer renderer){
        spectrumRenderer.setRenderer(renderer);
    }
    public static AbstractSpectrumRenderer getSpectrum(){
        return spectrumRenderer.getRenderer();
    }
}
