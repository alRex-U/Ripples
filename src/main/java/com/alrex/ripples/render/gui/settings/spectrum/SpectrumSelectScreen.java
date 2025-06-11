package com.alrex.ripples.render.gui.settings.spectrum;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.render.gui.base.HeaderAbstractSettingScreen;
import com.alrex.ripples.render.gui.base.SelectItemScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class SpectrumSelectScreen extends SelectItemScreen {
    public SpectrumSelectScreen() {
        super(Component.translatable("ripples.setting.spectrum.type.title"));
    }

    @Override
    public List<ItemEntry> createEntries() {
        return RipplesSpectrumRegistry.get().getRegisteredSpectrumIDs().stream().map(it->new ItemEntry(
                Component.translatable(RipplesSpectrumRegistry.get().getSpectrumTranslationKey(it)),
                ()->{
                    RipplesConfig.setSpectrumID(it);
                    AudioManager.getInstance().notifyConfigChanged();
                }
        )).toList();
    }
}
