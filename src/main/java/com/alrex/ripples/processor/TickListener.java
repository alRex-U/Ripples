package com.alrex.ripples.processor;

import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.render.hud.HUDRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TickListener {
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event){
        if (event.phase == TickEvent.Phase.END)return;

        AudioManager.getInstance().tick();
    }

    @SubscribeEvent
    public static void onGameTick(TickEvent.LevelTickEvent event){
        HUDRegistry.tick();
    }
}
