package com.alrex.ripples.render.gui;

import com.alrex.ripples.RipplesConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class SpectrumDataSizeScaleSettingScreen extends HeaderedAbstractSpectrumSettingScreen{
    protected SpectrumDataSizeScaleSettingScreen() {
        super(Component.literal("Change Spectrum Data Size Factor"));
    }

    private static final int MARGIN=0;
    private ForgeSlider slider;
    @Override
    protected void init() {
        super.init();
        addRenderableWidget(slider=new ForgeSlider(
                MARGIN,
                HEADER_HEIGHT,
                this.width-MARGIN*2,
                font.lineHeight*2,
                Component.literal("Factor : "),
                Component.empty(),
                0.,1., RipplesConfig.CLIP_FT_SIZE.get(),
                0.02,2,true
        ));
    }

    @Override
    protected boolean placeCloseButton() {
        return true;
    }

    @Override
    protected void onCloseButtonPressed() {
        if (slider!=null){
            var value=slider.getValue();
            RipplesConfig.CLIP_FT_SIZE.set(value);
        }
        if (minecraft!=null){
            minecraft.setScreen(new RipplesSettingScreen());
        }
    }

    @Override
    protected void renderContent(GuiGraphics graphics, int contentOffsetX, int contentOffsetY, int contentWidth, int contentHeight, int mouseX, int mouseY, float partial) {

    }
}
