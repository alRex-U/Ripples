package com.alrex.ripples.mixin.audio;

import com.alrex.ripples.audio.LoadedSound;
import com.alrex.ripples.audio.SoundBufferDataManager;
import com.mojang.blaze3d.audio.OggAudioStream;
import com.mojang.blaze3d.audio.SoundBuffer;
import net.minecraft.Util;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Mixin(SoundBufferLibrary.class)
public class SoundBufferLibraryMixin {
    @Shadow @Final private Map<ResourceLocation, CompletableFuture<SoundBuffer>> cache;

    @Shadow @Final private ResourceProvider resourceManager;

    @Inject(method = "clear",at=@At("HEAD"))
    public void onClear(CallbackInfo ci){
        SoundBufferDataManager.getInstance().clear();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public CompletableFuture<SoundBuffer> getCompleteBuffer(ResourceLocation location) {
        return (CompletableFuture<SoundBuffer>)this.cache.computeIfAbsent(location, (resourceLocation) -> CompletableFuture.supplyAsync(() -> {
            try {
                InputStream inputStream = this.resourceManager.open(resourceLocation);

                SoundBuffer soundBuffer;
                try {
                    OggAudioStream audioStream = new OggAudioStream(inputStream);

                    try {
                        ByteBuffer buffer = audioStream.readAll();
                        SoundBufferDataManager.getInstance()
                                .registerLoadedSound(
                                        location,
                                        new LoadedSound(
                                                resourceLocation,
                                                buffer,
                                                audioStream.getFormat()
                                        )
                                );
                        buffer.position(0);
                        soundBuffer = new SoundBuffer(buffer, audioStream.getFormat());
                    } catch (Throwable e) {
                        try {
                            audioStream.close();
                        } catch (Throwable e1) {
                            e.addSuppressed(e1);
                        }

                        throw e;
                    }

                    audioStream.close();
                } catch (Throwable e) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var6) {
                            e.addSuppressed(var6);
                        }
                    }

                    throw e;
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                return soundBuffer;
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }, Util.backgroundExecutor()));
    }

}
