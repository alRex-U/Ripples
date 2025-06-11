package com.alrex.ripples.render.gui.settings.spectrum;

import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.gui.base.DoubleSettingScreen;
import com.alrex.ripples.render.gui.base.HeaderAbstractSettingScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import java.awt.geom.QuadCurve2D;

public class SpectrumOpacitySetScreen extends DoubleSettingScreen {
    protected SpectrumOpacitySetScreen() {
        super(
                Component.translatable("ripples.setting.spectrum.opacity.title"),
                Component.translatable("ripples.setting.spectrum.opacity.prefix"),
                Component.empty(),
                0.,
                1.,
                RipplesConfig.SPECTRUM_OPACITY.get(),
                0.02,
                2
        );
    }

    @Override
    protected void onValueDetermined(double value) {
        RipplesConfig.SPECTRUM_OPACITY.set(value);
    }
}
