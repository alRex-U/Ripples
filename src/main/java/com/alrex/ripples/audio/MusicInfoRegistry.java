package com.alrex.ripples.audio;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.TreeMap;

public class MusicInfoRegistry {
    private final MusicInfoRegistry instance=new MusicInfoRegistry();

    public MusicInfoRegistry getInstance() {
        return instance;
    }

    private final TreeMap<ResourceLocation,MusicInfo> locationToInfoMap=new TreeMap<>();

    public void addInfo(ResourceLocation fileLocation,MusicInfo info){
        locationToInfoMap.put(fileLocation,info);
    }

    @Nullable
    public MusicInfo getInfo(ResourceLocation fileLocation){
        return locationToInfoMap.get(fileLocation);
    }
}
