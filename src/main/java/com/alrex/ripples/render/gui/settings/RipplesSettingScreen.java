package com.alrex.ripples.render.gui.settings;

import com.alrex.ripples.render.gui.base.SelectSettingScreen;
import com.alrex.ripples.render.gui.settings.spectrum.SpectrumSettingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class RipplesSettingScreen extends SelectSettingScreen {
    public RipplesSettingScreen() {
        super(Component.translatable("ripples.setting.title"));
    }

    @Override
    protected List<SettingEntry> createEntries() {
        return Arrays.asList(
                new SettingEntry(
                        Component.translatable("ripples.spectrum"),
                        Component.empty(),
                        ()->open(new SpectrumSettingScreen())
                ),
                new SettingEntry(
                        Component.translatable("ripples.sound_map"),
                        Component.empty(),
                        ()->{}
                ),
                new SettingEntry(
                        Component.translatable("ripples.content"),
                        Component.empty(),
                        ()->{}
                )
        );
    }
    private void open(Screen screen){
        if (minecraft!=null) minecraft.setScreen(screen);
    }
}
