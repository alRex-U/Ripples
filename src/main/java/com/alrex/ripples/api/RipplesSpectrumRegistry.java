package com.alrex.ripples.api;

import com.alrex.ripples.api.gui.AbstractSpectrumHUD;
import com.alrex.ripples.render.hud.spectrum.DefaultSpectrum;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;

public class RipplesSpectrumRegistry{
    private static final RipplesSpectrumRegistry instance=new RipplesSpectrumRegistry();
    private static final Supplier<AbstractSpectrumHUD> defaultHUDCtor= DefaultSpectrum::new;

    public static RipplesSpectrumRegistry get() {
        return instance;
    }

    private final TreeMap<ResourceLocation, Supplier<AbstractSpectrumHUD>> hudRegistry=new TreeMap<>();
    private boolean modInitialized=false;

    public void register(ResourceLocation id, Supplier<AbstractSpectrumHUD> constructor){
        hudRegistry.put(id, constructor);
    }

    public Set<ResourceLocation> getRegisteredEntries(){
        return hudRegistry.keySet();
    }

    public AbstractSpectrumHUD getHUD(ResourceLocation location){
        var hudSupplier=hudRegistry.get(location);
        if (hudSupplier == null)hudSupplier=defaultHUDCtor;
        return hudSupplier.get();
    }
}
