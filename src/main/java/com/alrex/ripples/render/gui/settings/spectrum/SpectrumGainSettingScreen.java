package com.alrex.ripples.render.gui.settings.spectrum;

import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.gui.base.DoubleSettingScreen;
import com.alrex.ripples.render.gui.base.HeaderAbstractSettingScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ForgeSlider;

@OnlyIn(Dist.CLIENT)
public class SpectrumGainSettingScreen extends DoubleSettingScreen {
    protected SpectrumGainSettingScreen() {
        super(
                Component.translatable("ripples.setting.spectrum.gain.title"),
                Component.translatable("ripples.setting.spectrum.gain.prefix"),
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
}
