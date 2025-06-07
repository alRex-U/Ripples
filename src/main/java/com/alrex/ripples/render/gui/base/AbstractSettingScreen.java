package com.alrex.ripples.render.gui.base;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class AbstractSettingScreen extends Screen {

    @Nullable
    private Supplier<Screen> backScreenSupplier;

    public AbstractSettingScreen backToWhenClosed(Supplier<Screen> screenSupplier){
        this.backScreenSupplier=screenSupplier;
        return this;
    }

    @Override
    public void onClose() {
        if (minecraft!=null && backScreenSupplier!=null){
            minecraft.setScreen(backScreenSupplier.get());
        }else {
            super.onClose();
        }
    }

    protected AbstractSettingScreen(Component p_96550_) {
        super(p_96550_);
    }
    @Override
    public void renderBackground(GuiGraphics graphics) {
        graphics.fill(0, 0, this.width, this.height, 0x77000000);
    }
}
