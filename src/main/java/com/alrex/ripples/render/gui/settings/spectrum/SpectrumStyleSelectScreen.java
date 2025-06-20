package com.alrex.ripples.render.gui.settings.spectrum;

import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.api.gui.SpectrumStyle;
import com.alrex.ripples.render.gui.base.HeaderAbstractSettingScreen;
import com.alrex.ripples.render.gui.base.SelectItemScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SpectrumStyleSelectScreen extends SelectItemScreen {
    public SpectrumStyleSelectScreen() {
        super(Component.translatable("ripples.setting.spectrum.style.title"));
    }

    @Override
    public List<ItemEntry> createEntries() {
        return Arrays.stream(SpectrumStyle.values())
                .map(it->new ItemEntry(
                        Component.translatable("ripples.spectrum.style."+it.toString().toLowerCase()),
                        ()->RipplesConfig.SPECTRUM_STYLE.set(it)
                ))
                .toList();
    }
}
