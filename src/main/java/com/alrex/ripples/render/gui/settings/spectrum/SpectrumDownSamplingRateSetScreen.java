package com.alrex.ripples.render.gui.settings.spectrum;

import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.gui.base.DoubleSettingScreen;
import net.minecraft.network.chat.Component;

public class SpectrumDownSamplingRateSetScreen extends DoubleSettingScreen {
    protected SpectrumDownSamplingRateSetScreen() {
        super(
                Component.translatable("ripples.setting.spectrum.down_sample.title"),
                Component.translatable("ripples.setting.spectrum.down_sample.prefix"),
                Component.empty(),
                0.1,
                1.,
                RipplesConfig.DOWN_SAMPLING_RATE.get(),
                0.02,
                2
        );
    }

    @Override
    protected void onValueDetermined(double value) {
        RipplesConfig.DOWN_SAMPLING_RATE.set(value);
    }
}
