package com.alrex.ripples.render.gui.settings.spectrum;

import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.gui.base.AbstractSettingScreen;
import com.alrex.ripples.render.gui.base.SelectSettingScreen;
import com.alrex.ripples.render.gui.settings.RipplesSettingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class SpectrumSettingScreen extends SelectSettingScreen {
    public SpectrumSettingScreen() {
        super(Component.translatable("ripples.setting.spectrum.title"));
    }
    @Override
    protected List<SettingEntry> createEntries() {
        return Arrays.asList(
                new SettingEntry(
                        Component.translatable("ripples.spectrum"),
                        Component.translatable(RipplesSpectrumRegistry.get().getSpectrumTranslationKey(RipplesConfig.getSpectrumID())),
                        ()->open(new SpectrumSelectScreen().backToWhenClosed(SpectrumSettingScreen::newAndBackToRootSettingWhenClosed))
                ),
                new SettingEntry(
                        Component.translatable("ripples.spectrum.style"),
                        Component.translatable("ripples.spectrum.style."+RipplesConfig.SPECTRUM_STYLE.get().toString().toLowerCase()),
                        ()->open(new SpectrumStyleSelectScreen().backToWhenClosed(SpectrumSettingScreen::newAndBackToRootSettingWhenClosed))
                ),
                new SettingEntry(
                        Component.translatable("ripples.spectrum.opacity"),
                        Component.literal(String.format("%.2f",RipplesConfig.SPECTRUM_OPACITY.get())),
                        ()->open(new SpectrumOpacitySetScreen().backToWhenClosed(SpectrumSettingScreen::newAndBackToRootSettingWhenClosed))
                ),
                new SettingEntry(
                        Component.translatable("ripples.spectrum.gain"),
                        Component.literal(String.format("%.2f",RipplesConfig.SPECTRUM_GAIN.get())),
                        ()->open(new SpectrumGainSettingScreen().backToWhenClosed(SpectrumSettingScreen::newAndBackToRootSettingWhenClosed))
                ),
                new SettingEntry(
                        Component.translatable("ripples.spectrum.data_size_scale"),
                        Component.literal(String.format("%.2f",RipplesConfig.CLIP_FT_SIZE.get())),
                        ()->open(new SpectrumDataSizeScaleSettingScreen().backToWhenClosed(SpectrumSettingScreen::newAndBackToRootSettingWhenClosed))
                )
        );
    }

    private void open(Screen screen){
        if (minecraft!=null) minecraft.setScreen(screen);
    }
    private static AbstractSettingScreen newAndBackToRootSettingWhenClosed(){
        return new SpectrumSettingScreen().backToWhenClosed(RipplesSettingScreen::new);
    }
}
