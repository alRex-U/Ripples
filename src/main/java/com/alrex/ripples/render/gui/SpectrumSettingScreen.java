package com.alrex.ripples.render.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class SpectrumSettingScreen extends HeaderedAbstractSpectrumSettingScreen {
    public SpectrumSettingScreen() {
        super(Component.literal("Ripples Setting"));
    }

    @Override
    protected void renderContent(GuiGraphics graphics, int contentOffsetX, int contentOffsetY, int contentWidth, int contentHeight, int mouseX, int mouseY, float partial) {

    }
}
