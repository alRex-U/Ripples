package com.alrex.ripples.processor;

import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.input.KeyBindings;
import com.alrex.ripples.render.gui.SpectrumSelectScreen;
import com.alrex.ripples.render.gui.SpectrumSettingScreen;
import com.alrex.ripples.render.gui.SpectrumStyleSelectScreen;
import com.alrex.ripples.render.hud.HUDRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TickListener {
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event){
        if (event.phase == TickEvent.Phase.END)return;

        AudioManager.getInstance().tick();

        if (KeyBindings.getOpenSettingKey().isDown()){
            Minecraft.getInstance().setScreen(new SpectrumStyleSelectScreen());
        }
    }

    @SubscribeEvent
    public static void onGameTick(TickEvent.LevelTickEvent event){
        HUDRegistry.tick();
    }
}
