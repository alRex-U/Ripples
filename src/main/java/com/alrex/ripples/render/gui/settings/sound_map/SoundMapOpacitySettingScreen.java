package com.alrex.ripples.render.gui.settings.sound_map;

import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.gui.base.DoubleSettingScreen;
import net.minecraft.network.chat.Component;

public class SoundMapOpacitySettingScreen extends DoubleSettingScreen {
    public SoundMapOpacitySettingScreen() {
        super(
                Component.literal("Opacity"),
                Component.literal("Opacity : "),
                Component.empty(),
                0d,
                1d,
                RipplesConfig.SOUND_MAP_OPACITY.get(),
                0.02,
                2
        );
    }

    @Override
    protected void onValueDetermined(double value) {
        RipplesConfig.SOUND_MAP_OPACITY.set(value);
    }
}
