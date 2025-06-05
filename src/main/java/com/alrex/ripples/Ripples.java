package com.alrex.ripples;

import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.input.KeyBindings;
import com.alrex.ripples.processor.SoundEventListener;
import com.alrex.ripples.processor.TickListener;
import com.alrex.ripples.render.hud.HUDRegistry;
import com.alrex.ripples.render.hud.spectrum.AutomataSpectrum;
import com.alrex.ripples.render.hud.spectrum.CircleSpectrum;
import com.alrex.ripples.render.hud.spectrum.HotbarSpectrum;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
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
        MinecraftForge.EVENT_BUS.register(TickListener.class);
        MinecraftForge.EVENT_BUS.register(SoundEventListener.class);

        context.getModEventBus().register(KeyBindings.class);
        context.getModEventBus().register(HUDRegistry.class);

        RipplesSpectrumRegistry.get().register(AutomataSpectrum.SPECTRUM_ID,AutomataSpectrum::new);
        RipplesSpectrumRegistry.get().register(HotbarSpectrum.SPECTRUM_ID,HotbarSpectrum::new);
        RipplesSpectrumRegistry.get().register(CircleSpectrum.SPECTRUM_ID,CircleSpectrum::new);

        RipplesConfig.register(context);
    }
}
