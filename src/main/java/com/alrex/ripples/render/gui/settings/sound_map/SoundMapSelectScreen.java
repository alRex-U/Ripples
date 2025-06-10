package com.alrex.ripples.render.gui.settings.sound_map;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.gui.base.SelectItemScreen;
import com.alrex.ripples.render.gui.base.SelectSettingScreen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class SoundMapSelectScreen extends SelectItemScreen {
    public SoundMapSelectScreen() {
        super(Component.translatable("ripples.setting.sound_map.type.title"));
    }

    @Override
    public List<ItemEntry> createEntries() {
        return RipplesSpectrumRegistry
                .get()
                .getRegisteredSoundMapIDs()
                .stream()
                .map(it->new ItemEntry(
                        Component.translatable(RipplesSpectrumRegistry.get().getSoundMapTranslationKey(it)),
                        ()-> {
                            RipplesConfig.setSoundMapID(it);
                            AudioManager.getInstance().notifyConfigChanged();
                        }
                ))
                .toList();
    }
}
