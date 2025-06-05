package com.alrex.ripples.render.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class HeaderedAbstractSpectrumSettingScreen extends AbstractSpectrumSettingScreen {
    protected HeaderedAbstractSpectrumSettingScreen(Component p_96550_) {
        super(p_96550_);
    }

    protected int HEADER_HEIGHT;

    @Override
    protected void init() {
        super.init();
        HEADER_HEIGHT=font.lineHeight*2;
    }

    @Override
    public final void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        super.render(graphics, mouseX, mouseY, partial);

        renderContent(graphics,0,HEADER_HEIGHT,this.width,this.height-HEADER_HEIGHT,mouseX,mouseY,partial);
        renderHeader(graphics, mouseX, mouseY, partial);
    }

    public void renderHeader(GuiGraphics graphics, int mouseX, int mouseY, float partial){
        graphics.fillGradient(0,0,this.width,HEADER_HEIGHT,0xFF444444,0xFF666666);
        graphics.drawString(font,this.title,HEADER_HEIGHT/4+1,HEADER_HEIGHT/4+1,0xFFEEEEEE);
    }

    protected abstract void renderContent(GuiGraphics graphics, int contentOffsetX,int contentOffsetY,int contentWidth,int contentHeight, int mouseX, int mouseY, float partial);
}
