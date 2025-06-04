package com.alrex.ripples.audio;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentSkipListMap;

public class SoundBufferDataManager {
    private static final SoundBufferDataManager instance=new SoundBufferDataManager();
    public static SoundBufferDataManager getInstance(){
        return instance;
    }

    private final ConcurrentSkipListMap<ResourceLocation, LoadedSound> locationToDataMap=new ConcurrentSkipListMap<>();

    public void registerLoadedSound(ResourceLocation location, LoadedSound data){
        locationToDataMap.put(location,data);
    }

    @Nullable
    public LoadedSound getSound(ResourceLocation location){
        return locationToDataMap.get(location);
    }

    public void clear(){
        locationToDataMap.clear();
    }
}
