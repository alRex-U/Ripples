package com.alrex.ripples.listener;

import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.audio.SoundBufferDataManager;
import com.alrex.ripples.render.hud.HUDRegistry;
import com.alrex.ripples.resources.MusicInfoManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class SoundEventListener {
    @SubscribeEvent
    public static void onStaticSoundPlay(PlaySoundSourceEvent event){
        var sound= SoundBufferDataManager.getInstance().getSound(event.getSound().getSound().getPath());
        if (sound == null || event.getSound().isLooping()) {
            return;
        }
        // streaming source registration is done in ChannelMixin class
        AudioManager.getInstance().registerAudioSource(
                event.getChannel().source,
                sound.getInstance(event.getChannel(),event.getSound())
        );
    }
    @SubscribeEvent
    public static void onStreamSoundPlay(PlayStreamingSourceEvent event){
        if (Minecraft.getInstance().player==null)return;
        HUDRegistry.notifyStartSoundStream(event.getSound().getSound().getPath());
    }
}
