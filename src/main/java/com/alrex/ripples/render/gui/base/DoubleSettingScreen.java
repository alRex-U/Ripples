package com.alrex.ripples.render.gui.base;

import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.gui.settings.spectrum.SpectrumSettingScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ForgeSlider;

@OnlyIn(Dist.CLIENT)
public abstract class DoubleSettingScreen extends HeaderAbstractSettingScreen{

    private static final int MARGIN=0;
    private ForgeSlider slider;
    private final double min;
    private final double max;
    private final double startValue;
    private final double stepSize;
    private final int precision;
    private final Component prefix;
    private final Component suffix;

    protected DoubleSettingScreen(
            Component title,
            Component prefix,
            Component suffix,
            double min, double max, double startValue,
            double stepSize, int precision
    ) {
        super(title);
        this.min=min;
        this.max=max;
        this.startValue=startValue;
        this.stepSize=stepSize;
        this.precision=precision;
        this.prefix=prefix;
        this.suffix=suffix;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(slider=new ForgeSlider(
                MARGIN,
                font.lineHeight*2,
                this.width-MARGIN*2,
                font.lineHeight*2,
                prefix,
                suffix,
                min,max,startValue,
                stepSize,precision,true
        ));
    }

    protected abstract void onValueDetermined(double value);

    @Override
    public void onClose() {
        onValueDetermined(slider.getValue());
        super.onClose();
    }

    @Override
    protected boolean placeCloseButton() {
        return true;
    }

    @Override
    protected void renderContent(GuiGraphics graphics, int contentOffsetX, int contentOffsetY, int contentWidth, int contentHeight, int mouseX, int mouseY, float partial) {

    }

}
