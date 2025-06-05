package com.alrex.ripples.render.gui;

import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.api.gui.SpectrumStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;

public class SpectrumStyleSelectScreen extends HeaderedAbstractSpectrumSettingScreen{
    public SpectrumStyleSelectScreen() {
        super(Component.literal("Select Spectrum Style"));
    }
    private int LIST_Y_MARGIN;

    @Override
    protected void init() {
        super.init();
        LIST_Y_MARGIN=HEADER_HEIGHT;
        this.addRenderableWidget(listComponent=new SpectrumStyleSelectionList(this.minecraft));
    }

    @Override
    protected boolean placeCloseButton() {
        return true;
    }

    @Override
    protected void onCloseButtonPressed() {
        SpectrumStyleSelectScreen.SpectrumStyleSelectionList.Entry selected=listComponent.getSelected();
        if (selected!=null){
            RipplesConfig.SPECTRUM_STYLE.set(selected.getStyle());
        }

        if (this.minecraft!=null){
            this.minecraft.setScreen(new RipplesSettingScreen());
        }
    }

    private final List<ResourceLocation> idList= RipplesSpectrumRegistry.get().getRegisteredEntries().stream().toList();
    private SpectrumStyleSelectScreen.SpectrumStyleSelectionList listComponent;

    @Override
    protected void renderContent(GuiGraphics graphics, int contentOffsetX, int contentOffsetY, int contentWidth, int contentHeight, int mouseX, int mouseY, float partial) {
    }

    private static final int LIST_ITEM_HEIGHT = Minecraft.getInstance().font.lineHeight*2;

    private class SpectrumStyleSelectionList extends ObjectSelectionList<SpectrumStyleSelectionList.Entry> {
        public SpectrumStyleSelectionList(Minecraft mc) {
            super(
                    mc,
                    SpectrumStyleSelectScreen.this.width, //component width
                    SpectrumStyleSelectScreen.this.height, //component height
                    LIST_Y_MARGIN, // render area start y
                    SpectrumStyleSelectScreen.this.height, // render area end y
                    LIST_ITEM_HEIGHT //item height
            );
            setRenderBackground(false);
            setRenderTopAndBottom(false);
            setRenderHeader(false,0);
            Arrays.stream(SpectrumStyle.values()).forEach(it->this.addEntry(new SpectrumStyleSelectionList.Entry(it)));
        }

        private class Entry extends ObjectSelectionList.Entry<SpectrumStyleSelectionList.Entry>{
            private final Component narration;
            private final Component name;
            private final SpectrumStyle style;
            public Entry(SpectrumStyle style){
                this.style=style;
                narration=Component.literal(style.toString());
                name=Component.translatable("ripples.spectrum.style."+style.toString().toLowerCase());
            }

            public SpectrumStyle getStyle() {
                return style;
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int scroll) {
                if (scroll==0){
                    SpectrumStyleSelectScreen.SpectrumStyleSelectionList.this.setSelected(this);
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
                guiGraphics.drawCenteredString(SpectrumStyleSelectScreen.this.font, this.name, SpectrumStyleSelectionList.this.width / 2, y + itemHeight/2 - font.lineHeight/2, 0xFFFFFF);
            }
        }
    }
}
