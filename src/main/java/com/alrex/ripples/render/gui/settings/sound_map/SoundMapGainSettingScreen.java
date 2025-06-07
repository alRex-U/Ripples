package com.alrex.ripples.render.gui.settings.sound_map;

import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.gui.base.DoubleSettingScreen;
import com.alrex.ripples.render.gui.base.SelectSettingScreen;
import net.minecraft.network.chat.Component;

public class SoundMapGainSettingScreen extends DoubleSettingScreen {
    public SoundMapGainSettingScreen() {
        super(
                Component.literal("gain"),
                Component.literal("Gain : "),
                Component.empty(),
                0d,
                10d,
                RipplesConfig.SOUND_MAP_GAIN.get(),
                0.02,
                2
        );
    }

    @Override
    protected void onValueDetermined(double value) {
        RipplesConfig.SOUND_MAP_GAIN.set(value);
    }
}
