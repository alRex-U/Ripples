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
public class SpectrumDataSizeScaleSettingScreen extends DoubleSettingScreen {
    protected SpectrumDataSizeScaleSettingScreen() {
        super(
                Component.translatable("ripples.setting.spectrum.ft_size.title"),
                Component.translatable("ripples.setting.spectrum.ft_size.prefix"),
                Component.empty(),
                0.,
                1.,
                RipplesConfig.CLIP_FT_SIZE.get(),
                0.02,
                2
        );
    }

    @Override
    protected void onValueDetermined(double value) {
        RipplesConfig.CLIP_FT_SIZE.set(value);
    }
}
