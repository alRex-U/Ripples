package com.alrex.ripples.render.gui.settings.spectrum;

import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.gui.base.DoubleSettingScreen;
import com.alrex.ripples.render.gui.base.HeaderAbstractSettingScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class SpectrumGainSettingScreen extends DoubleSettingScreen {
    protected SpectrumGainSettingScreen() {
        super(
                Component.literal("Change Spectrum Gain"),
                Component.literal("Gain : "),
                Component.empty(),
                0.,
                10.,
                RipplesConfig.SPECTRUM_GAIN.get(),
                0.02,
                2
        );
    }

    @Override
    protected void onValueDetermined(double value) {
        RipplesConfig.SPECTRUM_GAIN.set(value);
    }

    @Override
    protected void onCloseButtonPressed() {
        if (minecraft!=null){
            minecraft.setScreen(new SpectrumSettingScreen());
        }
    }
}
