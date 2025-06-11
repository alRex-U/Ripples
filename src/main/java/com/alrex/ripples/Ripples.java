package com.alrex.ripples;

import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.input.KeyBindings;
import com.alrex.ripples.listener.SoundEventListener;
import com.alrex.ripples.listener.TickListener;
import com.alrex.ripples.render.hud.HUDRegistry;
import com.alrex.ripples.render.hud.soundmap.CircleSoundMap;
import com.alrex.ripples.render.hud.soundmap.HorizontalSoundMap;
import com.alrex.ripples.render.hud.soundmap.RadarSoundMap;
import com.alrex.ripples.render.hud.spectrum.AutomataSpectrum;
import com.alrex.ripples.render.hud.spectrum.CircleSpectrum;
import com.alrex.ripples.render.hud.spectrum.HotbarSpectrum;
import com.alrex.ripples.resources.MusicInfoManager;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Ripples.MOD_ID)
public class Ripples
{
    public static final String MOD_ID = "ripples";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Ripples(FMLJavaModLoadingContext context)
    {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, ()->(()->this.init(context)));
    }

    private void init(FMLJavaModLoadingContext context){
        MinecraftForge.EVENT_BUS.register(TickListener.class);
        MinecraftForge.EVENT_BUS.register(SoundEventListener.class);

        context.getModEventBus().register(KeyBindings.class);
        context.getModEventBus().register(HUDRegistry.class);
        context.getModEventBus().addListener(this::onRegisterResourceReloadListener);

        RipplesSpectrumRegistry.get().registerSpectrum(AutomataSpectrum.SPECTRUM_ID,AutomataSpectrum::new);
        RipplesSpectrumRegistry.get().registerSpectrum(HotbarSpectrum.SPECTRUM_ID,HotbarSpectrum::new);
        RipplesSpectrumRegistry.get().registerSpectrum(CircleSpectrum.SPECTRUM_ID, CircleSpectrum::normal);
        RipplesSpectrumRegistry.get().registerSpectrum(CircleSpectrum.SPECTRUM_ID_MIRRORED, CircleSpectrum::mirrored);

        RipplesSpectrumRegistry.get().registerSoundMap(CircleSoundMap.SOUND_MAP_ID,CircleSoundMap::new);
        RipplesSpectrumRegistry.get().registerSoundMap(RadarSoundMap.SOUND_MAP_ID,RadarSoundMap::new);
        RipplesSpectrumRegistry.get().registerSoundMap(HorizontalSoundMap.SOUND_MAP_ID,HorizontalSoundMap::new);

        RipplesConfig.register(context);
    }
    @OnlyIn(Dist.CLIENT)
    private void onRegisterResourceReloadListener(RegisterClientReloadListenersEvent event){
        event.registerReloadListener(MusicInfoManager.get());
    }
}
