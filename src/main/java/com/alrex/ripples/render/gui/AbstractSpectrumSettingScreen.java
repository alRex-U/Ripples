package com.alrex.ripples.render.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Stack;

public class AbstractSpectrumSettingScreen extends Screen {
    private Stack<AbstractSpectrumSettingScreen> screenStack=new Stack<>();

    protected AbstractSpectrumSettingScreen(Component p_96550_) {
        super(p_96550_);
        screenStack.push(this);
    }
    @Override
    public void renderBackground(GuiGraphics graphics) {
        graphics.fill(0, 0, this.width, this.height, 0x44000000);
    }
}
