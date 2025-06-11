package com.alrex.ripples.render.gui.settings;

import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.gui.base.SelectSettingScreen;
import com.alrex.ripples.render.gui.settings.sound_map.SoundMapSettingScreen;
import com.alrex.ripples.render.gui.settings.spectrum.SpectrumSettingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class RipplesSettingScreen extends SelectSettingScreen {
    public RipplesSettingScreen() {
        super(Component.translatable("ripples.setting.title"));
    }

    @Override
    protected List<SettingEntry> createEntries() {
        return Arrays.asList(
                new SettingEntry(
                        Component.translatable("ripples.content"),
                        Component.translatable(RipplesConfig.CONTENT_TYPE.get().getTranslationKey()),
                        ()->open(new RipplesContentSelectScreen().backToWhenClosed(RipplesSettingScreen::new))
                ),
                new SettingEntry(
                        Component.translatable("ripples.setting.spectrum.title"),
                        TAIL_NAVIGATION,
                        ()->open(new SpectrumSettingScreen().backToWhenClosed(RipplesSettingScreen::new))
                ),
                new SettingEntry(
                        Component.translatable("ripples.setting.sound_map.title"),
                        TAIL_NAVIGATION,
                        ()->open(new SoundMapSettingScreen().backToWhenClosed(RipplesSettingScreen::new))
                ),
                new SettingEntry(
                        Component.translatable("ripples.color_pallet"),
                        TAIL_NAVIGATION,
                        ()->open(new RipplesColorPalletScreen().backToWhenClosed(RipplesSettingScreen::new))
                )
        );
    }
    private void open(Screen screen){
        if (minecraft!=null) minecraft.setScreen(screen);
    }
}
