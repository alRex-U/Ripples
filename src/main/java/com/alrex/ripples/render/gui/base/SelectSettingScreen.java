package com.alrex.ripples.render.gui.base;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class SelectSettingScreen extends HeaderAbstractSettingScreen{
    protected SelectSettingScreen(Component title) {
        super(title);
        entries=this.createEntries();
    }

    protected abstract List<SettingEntry> createEntries();

    private static final int MARGIN=5;
    private final List<SettingEntry> entries;
    private int selected=-1;
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
            graphics.drawString(font,entry.tail(),contentOffsetX+contentWidth-MARGIN-font.width(entry.tail()),textY,0xFFFFFF);
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

    public record SettingEntry(Component name, Component tail, Runnable onSelected){
    }
}
