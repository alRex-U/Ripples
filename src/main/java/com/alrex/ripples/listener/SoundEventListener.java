package com.alrex.ripples.listener;

import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.audio.SoundBufferDataManager;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.client.event.sound.PlayStreamingSourceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SoundEventListener {
    @SubscribeEvent
    public static void onStaticSoundPlay(PlaySoundSourceEvent event){
        var sound= SoundBufferDataManager.getInstance().getSound(event.getSound().getSound().getPath());
        if (sound == null) {
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
    }
}
