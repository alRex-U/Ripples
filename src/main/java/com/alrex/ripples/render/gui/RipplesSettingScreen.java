package com.alrex.ripples.render.gui;

import com.alrex.ripples.RipplesConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class RipplesSettingScreen extends HeaderedAbstractSpectrumSettingScreen {
    private final List<SettingEntry> entries;
    private int selected=-1;

    public RipplesSettingScreen() {
        super(Component.literal("Ripples Setting"));
        entries=loadEntries();
    }


    private static final int MARGIN=5;
    private int itemHeight;

    @Override
    protected void init() {
        super.init();
        itemHeight= font.lineHeight*2;
    }

    @Override
    protected void renderContent(GuiGraphics graphics, int contentOffsetX, int contentOffsetY, int contentWidth, int contentHeight, int mouseX, int mouseY, float partial) {
        selected=-1;
        for(var i=0;i<entries.size();i++){
            int itemOffsetY=contentOffsetY+itemHeight*i;
            int textY=itemOffsetY+itemHeight/2-font.lineHeight/2;
            var entry=entries.get(i);
            int relativeMouseX=mouseX-contentOffsetX;
            int relativeMouseY=mouseY-itemOffsetY;
            if (0< relativeMouseX && relativeMouseX<contentWidth && 0<relativeMouseY && relativeMouseY<itemHeight){
                selected=i;
                graphics.fill(contentOffsetX,itemOffsetY,contentOffsetX+contentWidth,itemOffsetY+itemHeight,0x33FFFFFF);
            }
            graphics.drawString(font,entry.name(),contentOffsetX+MARGIN,textY,0xFFFFFF);
            graphics.drawString(font,entry.value(),contentOffsetX+contentWidth-MARGIN-font.width(entry.value()),textY,0xFFFFFF);
            graphics.hLine(contentOffsetX,contentOffsetX+contentWidth,itemOffsetY+itemHeight,0xFFFFFFFF);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int scroll) {
        if (scroll ==0 && selected >=0){
            entries.get(selected).onSelected().run();
        }
        return super.mouseClicked(mouseX, mouseY, scroll);
    }

    private List<SettingEntry> loadEntries(){
        return Arrays.asList(
                new SettingEntry(
                        Component.translatable("ripples.spectrum"),
                        Component.translatable("ripples.spectrum."+ RipplesConfig.getSpectrumID().getPath().toLowerCase()),
                        ()->open(new SpectrumSelectScreen())
                ),
                new SettingEntry(
                        Component.translatable("ripples.spectrum.style"),
                        Component.translatable("ripples.spectrum.style."+RipplesConfig.SPECTRUM_STYLE.get().toString().toLowerCase()),
                        ()->open(new SpectrumStyleSelectScreen())
                ),
                new SettingEntry(
                        Component.translatable("ripples.spectrum.opacity"),
                        Component.literal(String.format("%.2f",RipplesConfig.SPECTRUM_OPACITY.get())),
                        ()->open(new SpectrumOpacitySetScreen())
                ),
                new SettingEntry(
                        Component.translatable("ripples.spectrum.gain"),
                        Component.literal(String.format("%.2f",RipplesConfig.SPECTRUM_GAIN.get())),
                        ()->open(new SpectrumGainSettingScreen())
                ),
                new SettingEntry(
                        Component.translatable("ripples.spectrum.data_size_scale"),
                        Component.literal(String.format("%.2f",RipplesConfig.CLIP_FT_SIZE.get())),
                        ()->open(new SpectrumDataSizeScaleSettingScreen())
                )
        );
    }
    private void open(Screen screen){
        if (minecraft == null)return;
        minecraft.setScreen(screen);
    }

    private record SettingEntry(Component name, Component value, Runnable onSelected){
    }
}
