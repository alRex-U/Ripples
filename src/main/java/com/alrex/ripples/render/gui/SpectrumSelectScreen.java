package com.alrex.ripples.render.gui;

import com.alrex.ripples.RipplesConfig;
import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.render.hud.HUDRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpectrumSelectScreen extends HeaderedAbstractSpectrumSettingScreen {
    public SpectrumSelectScreen() {
        super(Component.literal("Ripples Setting Select Spectrum"));
    }
    private int LIST_Y_MARGIN;

    @Override
    protected void init() {
        super.init();
        LIST_Y_MARGIN=HEADER_HEIGHT;
        this.addWidget(listComponent=new SpectrumSelectionList(this.minecraft));
        int buttonHeight= font.lineHeight*2;
        int buttonWidth=font.width("Done");
        this.addRenderableWidget(
                new PlainTextButton(
                        this.width-5-buttonWidth,
                        5,
                        buttonWidth,
                        buttonHeight,
                        Component.literal("Done"),
                        (it)-> onDone(),
                        font
                )
        );
    }

    void onDone() {
        SpectrumSelectionList.Entry selected=listComponent.getSelected();
        if (selected!=null){
            RipplesConfig.setSpectrumID(selected.getLocation());
            HUDRegistry.setSpectrum(RipplesSpectrumRegistry.get().getHUD(selected.location));
        }

        if (this.minecraft!=null){
            this.minecraft.popGuiLayer();
        }
    }

    private final List<ResourceLocation> idList= RipplesSpectrumRegistry.get().getRegisteredEntries().stream().toList();
    private SpectrumSelectionList listComponent;

    @Override
    protected void renderContent(GuiGraphics graphics, int contentOffsetX, int contentOffsetY, int contentWidth, int contentHeight, int mouseX, int mouseY, float partial) {
        renderBackground(graphics);
        listComponent.render(graphics, mouseX, mouseY, partial);
    }

    private static final int LIST_ITEM_HEIGHT = Minecraft.getInstance().font.lineHeight*2;

    private class SpectrumSelectionList extends ObjectSelectionList<SpectrumSelectionList.Entry>{
        public SpectrumSelectionList(Minecraft mc) {
            super(
                    mc,
                    SpectrumSelectScreen.this.width, //component width
                    SpectrumSelectScreen.this.height, //component height
                    LIST_Y_MARGIN, // render area start y
                    SpectrumSelectScreen.this.height, // render area end y
                    LIST_ITEM_HEIGHT //item height
            );
            setRenderBackground(false);
            setRenderTopAndBottom(false);
            setRenderHeader(false,0);
            SpectrumSelectScreen.this.idList.forEach(it->this.addEntry(new Entry(it)));
        }

        @Override
        protected void renderBackground(GuiGraphics p_283512_) {}

        private class Entry extends ObjectSelectionList.Entry<Entry>{
            private final Component narration;
            private final Component name;
            private final ResourceLocation location;
            public Entry(ResourceLocation location){
                this.location=location;
                narration=Component.literal(location.toString());
                name=Component.translatable("ripples.spectrum."+location.getPath());
            }

            public ResourceLocation getLocation() {
                return location;
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int scroll) {
                if (scroll==0){
                    SpectrumSelectionList.this.setSelected(this);
                    return true;
                }
                return false;
            }

            @Override
            public Component getNarration() {
                return narration;
            }

            @Override
            public void render(GuiGraphics guiGraphics, int entryIndex, int y, int rowLeft, int rowWidth, int itemHeight, int mouseX, int mouseY, boolean b, float partial) {
                guiGraphics.drawCenteredString(SpectrumSelectScreen.this.font, this.name, SpectrumSelectScreen.SpectrumSelectionList.this.width / 2, y + itemHeight/2 - font.lineHeight/2, 0xFFFFFF);
            }
        }
    }
}
