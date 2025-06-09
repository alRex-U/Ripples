package com.alrex.ripples.render.gui.settings;

import com.alrex.ripples.audio.AudioManager;
import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.RenderContent;
import com.alrex.ripples.render.gui.base.SelectItemScreen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class RipplesContentSelectScreen extends SelectItemScreen {
    protected RipplesContentSelectScreen() {
        super(Component.literal("Content"));
    }

    @Override
    public List<ItemEntry> createEntries() {
        return Arrays.stream(RenderContent.values())
                .map(it->new ItemEntry(
                        Component.translatable("ripples.content."+it.toString().toLowerCase()),
                        ()->{
                            RipplesConfig.CONTENT_TYPE.set(it);
                            AudioManager.getInstance().notifyContentChanged();
                        }
                ))
                .toList();
    }
}
