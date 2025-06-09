package com.alrex.ripples.render.gui.settings.sound_map;

import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.gui.base.AbstractSettingScreen;
import com.alrex.ripples.render.gui.base.SelectSettingScreen;
import com.alrex.ripples.render.gui.settings.RipplesSettingScreen;
import com.alrex.ripples.render.gui.settings.spectrum.SpectrumSettingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SoundMapSettingScreen extends SelectSettingScreen {
    public SoundMapSettingScreen() {
        super(Component.translatable("ripples.setting.sound_map.title"));
    }

    @Override
    protected List<SettingEntry> createEntries() {
        return Arrays.asList(
                new SettingEntry(
                        Component.translatable("ripples.sound_map"),
                        Component.translatable(RipplesSpectrumRegistry.get().getSoundMapTranslationKey(RipplesConfig.getSoundMapID())),
                        ()->open(new SoundMapSelectScreen().backToWhenClosed(SoundMapSettingScreen::newAndBackToRootSettingWhenClosed))
                ),
                new SettingEntry(
                        Component.translatable("ripples.sound_map.opacity"),
                        Component.literal(String.format("%.2f",RipplesConfig.SOUND_MAP_OPACITY.get())),
                        ()->open(new SoundMapOpacitySettingScreen().backToWhenClosed(SoundMapSettingScreen::newAndBackToRootSettingWhenClosed))
                ),
                new SettingEntry(
                        Component.translatable("ripples.sound_map.gain"),
                        Component.literal(String.format("%.2f",RipplesConfig.SOUND_MAP_GAIN.get())),
                        ()->open(new SoundMapGainSettingScreen().backToWhenClosed(SoundMapSettingScreen::newAndBackToRootSettingWhenClosed))
                )
        );
    }

    private void open(Screen screen){
        if (minecraft!=null) minecraft.setScreen(screen);
    }
    private static AbstractSettingScreen newAndBackToRootSettingWhenClosed(){
        return new SoundMapSettingScreen().backToWhenClosed(RipplesSettingScreen::new);
    }
}
