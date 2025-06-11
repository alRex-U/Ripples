package com.alrex.ripples.render.gui.base;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class HeaderAbstractSettingScreen extends AbstractSettingScreen {
    protected HeaderAbstractSettingScreen(Component p_96550_) {
        super(p_96550_);
    }

    protected int headerHeight;
    protected int contentWidth;
    protected int contentHeight;
    protected int contentOffsetX;
    protected int contentOffsetY;

    @Override
    protected void init() {
        super.init();
        headerHeight =font.lineHeight*2;
        if (placeCloseButton()){
            int buttonHeight= font.lineHeight*2;
            int buttonWidth=font.width("Close");
            this.addRenderableWidget(
                    new PlainTextButton(
                            this.width-5-buttonWidth,
                            5,
                            buttonWidth,
                            buttonHeight,
                            Component.literal("Close"),
                            (it)-> {
                                playSelectionSound();
                                onCloseButtonPressed();
                            },
                            font
                    )
            );
        }
        contentOffsetX=0;
        contentOffsetY=headerHeight;
        contentWidth=this.width-contentOffsetX*2;
        contentHeight=this.height-contentOffsetY;

    }

    @Override
    public final void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partial);

        renderContent(graphics,contentOffsetX,contentOffsetY,contentWidth,contentHeight,mouseX,mouseY,partial);
        renderHeader(graphics, mouseX, mouseY, partial);
    }

    public void renderHeader(GuiGraphics graphics, int mouseX, int mouseY, float partial){
        graphics.fillGradient(0,0,this.width, headerHeight,0xDD444444,0xDD666666);
        graphics.drawString(font,this.title, headerHeight /4+1, headerHeight /4+1,0xEEEEEE);
    }

    protected abstract void renderContent(GuiGraphics graphics, int contentOffsetX,int contentOffsetY,int contentWidth,int contentHeight, int mouseX, int mouseY, float partial);
    protected boolean placeCloseButton(){
        return false;
    }
    protected void onCloseButtonPressed(){
        onClose();
    }
}
