package com.alrex.ripples.listener;

import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.input.KeyBindings;
import com.alrex.ripples.render.gui.settings.RipplesSettingScreen;
import com.alrex.ripples.render.gui.settings.spectrum.SpectrumSettingScreen;
import com.alrex.ripples.render.hud.HUDRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class TickListener {
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event){
        if (event.phase == TickEvent.Phase.END)return;

        AudioManager.getInstance().tick();
        HUDRegistry.tick();

        if (KeyBindings.getOpenSettingKey().isDown()){
            Minecraft.getInstance().setScreen(new RipplesSettingScreen());
        }
    }
}
