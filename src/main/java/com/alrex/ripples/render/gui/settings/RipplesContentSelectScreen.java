package com.alrex.ripples.render.gui.settings;

import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.RenderContent;
import com.alrex.ripples.render.gui.base.SelectItemScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class RipplesContentSelectScreen extends SelectItemScreen {
    protected RipplesContentSelectScreen() {
        super(Component.translatable("ripples.setting.content.title"));
    }

    @Override
    public List<ItemEntry> createEntries() {
        return Arrays.stream(RenderContent.values())
                .map(it->new ItemEntry(
                        Component.translatable(it.getTranslationKey()),
                        ()->{
                            RipplesConfig.CONTENT_TYPE.set(it);
                            AudioManager.getInstance().notifyContentChanged();
                        }
                ))
                .toList();
    }
}
