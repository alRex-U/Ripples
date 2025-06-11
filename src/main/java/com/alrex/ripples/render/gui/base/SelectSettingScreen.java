package com.alrex.ripples.render.gui.base;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class SelectSettingScreen extends HeaderAbstractSettingScreen{
    protected SelectSettingScreen(Component title) {
        super(title);
        entries=this.createEntries();
    }

    protected static final Component TAIL_NAVIGATION=Component.literal(">");

    protected abstract List<SettingEntry> createEntries();

    private static final int MARGIN=5;
    private final List<SettingEntry> entries;
    private int selected=-1;
    private int selectedTick=0;
    private int itemHeight;

    @Override
    protected void init() {
        super.init();
        itemHeight= font.lineHeight*2;
    }

    @Override
    public void tick() {
        super.tick();
        if (selected>=0){
            selectedTick++;
        }else {
            selectedTick=0;
        }
    }

    @Override
    protected void renderContent(GuiGraphics graphics, int contentOffsetX, int contentOffsetY, int contentWidth, int contentHeight, int mouseX, int mouseY, float partial) {
        boolean selection=false;
        for(var i=0;i<entries.size();i++){
            int itemOffsetY=contentOffsetY+itemHeight*i;
            int textY=itemOffsetY+itemHeight/2-font.lineHeight/2;
            var entry=entries.get(i);
            int relativeMouseX=mouseX-contentOffsetX;
            int relativeMouseY=mouseY-itemOffsetY;
            if (0< relativeMouseX && relativeMouseX<contentWidth && 0<relativeMouseY && relativeMouseY<itemHeight){
                selection=true;
                if (selected!=i) {
                    selected = i;
                    selectedTick=0;
                }
                float factor= 1-Mth.square(1-Mth.clamp(selectedTick+partial,0,6f)/6f);
                graphics.fill(contentOffsetX,itemOffsetY, (int) (contentOffsetX+contentWidth*factor),itemOffsetY+itemHeight,0x33FFFFFF);
            }
            graphics.drawString(font,entry.name(),contentOffsetX+MARGIN,textY,0xFFFFFF);
            graphics.drawString(font,entry.tail(),contentOffsetX+contentWidth-MARGIN-font.width(entry.tail()),textY,0xFFFFFF);
            graphics.hLine(contentOffsetX,contentOffsetX+contentWidth,itemOffsetY+itemHeight,0xAAFFFFFF);
        }
        if (!selection){
            selected=-1;
        }
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int scroll) {
        if (scroll ==0 && selected >=0){
            playSelectionSound();
            entries.get(selected).onSelected().run();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, scroll);
    }

    @Override
    protected boolean placeCloseButton() {
        return true;
    }

    public record SettingEntry(Component name, Component tail, Runnable onSelected){
    }
}
