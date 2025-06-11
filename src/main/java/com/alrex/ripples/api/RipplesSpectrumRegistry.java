package com.alrex.ripples.api;

import com.alrex.ripples.api.gui.AbstractSoundMapRenderer;
import com.alrex.ripples.api.gui.AbstractSpectrumRenderer;
import com.alrex.ripples.render.hud.soundmap.CircleSoundMap;
import com.alrex.ripples.render.hud.spectrum.HotbarSpectrum;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class RipplesSpectrumRegistry{
    private static final RipplesSpectrumRegistry instance=new RipplesSpectrumRegistry();
    private static final Supplier<AbstractSpectrumRenderer> defaultSpectrumCtor = HotbarSpectrum::new;
    private static final Supplier<AbstractSoundMapRenderer> defaultSoundMapCtor = CircleSoundMap::new;

    public static RipplesSpectrumRegistry get() {
        return instance;
    }

    private final TreeMap<ResourceLocation, Supplier<AbstractSpectrumRenderer>> spectrumRegistry =new TreeMap<>();
    private final TreeMap<ResourceLocation, Supplier<AbstractSoundMapRenderer>> soundMapRegistry =new TreeMap<>();

    public void registerSpectrum(ResourceLocation id, Supplier<AbstractSpectrumRenderer> constructor){
        spectrumRegistry.put(id, constructor);
    }
    public void registerSoundMap(ResourceLocation id, Supplier<AbstractSoundMapRenderer> constructor){
        soundMapRegistry.put(id,constructor);
    }

    public Set<ResourceLocation> getRegisteredSpectrumIDs(){
        return spectrumRegistry.keySet();
    }

    public Set<ResourceLocation> getRegisteredSoundMapIDs(){
        return soundMapRegistry.keySet();
    }

    public String getSpectrumTranslationKey(ResourceLocation location){
        return "ripples.spectrum.type."+location.getNamespace()+"."+location.getPath();
    }
    public String getSoundMapTranslationKey(ResourceLocation location){
        return "ripples.sound_map.type."+location.getNamespace()+"."+location.getPath();
    }

    public AbstractSpectrumRenderer getSpectrum(ResourceLocation location){
        var hudSupplier= spectrumRegistry.get(location);
        if (hudSupplier == null)hudSupplier= defaultSpectrumCtor;
        return hudSupplier.get();
    }

    public AbstractSoundMapRenderer getSoundMap(ResourceLocation location){
        var hudSupplier= soundMapRegistry.get(location);
        if (hudSupplier == null)hudSupplier= defaultSoundMapCtor;
        return hudSupplier.get();
    }
}
