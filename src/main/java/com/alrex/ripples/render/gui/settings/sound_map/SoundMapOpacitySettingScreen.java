package com.alrex.ripples.render.gui.settings.sound_map;

import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.gui.base.DoubleSettingScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoundMapOpacitySettingScreen extends DoubleSettingScreen {
    public SoundMapOpacitySettingScreen() {
        super(
                Component.translatable("ripples.setting.sound_map.opacity.title"),
                Component.translatable("ripples.setting.sound_map.opacity.prefix"),
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
