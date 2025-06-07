package com.alrex.ripples.resources;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class MusicInfoManager implements ResourceManagerReloadListener {
    private static MusicInfoManager instance;

    public static MusicInfoManager get() {
        if (instance==null){
            instance=new MusicInfoManager();
        }
        return instance;
    }

    private static final Logger LOGGER= LogUtils.getLogger();
    private static final Gson GSON=new Gson();
    private static final String MUSIC_INFO_FILE_PATH="ripples/music_info.json";

    private Map<ResourceLocation, MusicInfo> musicFileLocationToMusicInfoMap =new TreeMap<>();

    @Override
    public void onResourceManagerReload(@Nonnull ResourceManager resourceManager) {
        musicFileLocationToMusicInfoMap =extractMusicInfos(resourceManager);
    }

    @Nullable
    public MusicInfo getMusicInfo(ResourceLocation fileLocation){
        return musicFileLocationToMusicInfoMap.get(fileLocation);
    }

    private static Map<ResourceLocation, MusicInfo> extractMusicInfos(ResourceManager manager){
        Map<ResourceLocation,MusicInfo> resourceLocationMusicInfoMap=new TreeMap<>();
        for(var namespace:manager.getNamespaces()){
            var location=new ResourceLocation(namespace,MUSIC_INFO_FILE_PATH);
            var resources=manager.getResourceStack(location);
            for(var resource:resources){
                appendResource(resource,resourceLocationMusicInfoMap);
            }
        }
        return resourceLocationMusicInfoMap;
    }
    private static void appendResource(Resource resource,Map<ResourceLocation,MusicInfo> map){
        try{
            InputStream stream=resource.open();

            var result=loadFromJson(stream);
            map.putAll(result);

            stream.close();
        } catch (IOException e) {
            LOGGER.warn("Failed to music info file from pack {}", resource.sourcePackId(), e);
        }
    }
    private static Map<ResourceLocation, MusicInfo> loadFromJson(InputStream inputStream) throws IOException {
        JsonObject jsonobject = GSON.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);
        Map<ResourceLocation,MusicInfo> map=new TreeMap<>();
        for(var entry:jsonobject.entrySet()){
            var location=new ResourceLocation(entry.getKey());

            if (!(entry.getValue() instanceof JsonObject value)){
                throw new IOException(String.format("Property [%s] has to be a json object, but was %s",location,entry.getValue()));
            }

            var nameObj=value.get("name");
            String name;
            if (nameObj instanceof JsonPrimitive primitive && primitive.isString()){
                name= primitive.getAsString();
            }else {
                throw new IOException("Property 'name' has to be a string, but was ["+nameObj.toString()+"]");
            }

            var composerObj=value.get("composer");
            String composer;
            if (composerObj instanceof JsonPrimitive primitive1 && primitive1.isString()){
                composer=primitive1.getAsString();
            }else {
                throw new IOException("Property 'composer' has to be a string, but was ["+composerObj.toString()+"]");
            }

            map.put(location,new MusicInfo(name,composer));
        }
        return map;
    }

}
