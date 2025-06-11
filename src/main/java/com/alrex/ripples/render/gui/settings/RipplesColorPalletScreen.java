package com.alrex.ripples.render.gui.settings;

import com.alrex.ripples.api.gui.ColorPallet;
import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.render.gui.base.HeaderAbstractSettingScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import org.checkerframework.checker.units.qual.C;

import javax.annotation.Nullable;
import java.util.*;

public class RipplesColorPalletScreen extends HeaderAbstractSettingScreen {
    private static final int LIST_COLUMN_WIDTH=150;

    private final int maxColorCount=6;
    private final List<Integer> colors=new ArrayList<>(maxColorCount);
    @Nullable
    private Integer currentColor;
    @Nullable
    private Integer currentColorIndex;
    private int hovered =-1;
    private int hoveredTick =0;
    private int itemHeight;

    @Nullable
    private ForgeSlider rSlider,gSlider,bSlider;
    @Nullable
    private AbstractButton removeButton;
    @Nullable
    private StringWidget selectedColorString;

    protected RipplesColorPalletScreen() {
        super(Component.translatable("ripples.setting.color_pallet.title"));
        var pallet=RipplesConfig.getColorPallet();
        for (var i=0;i<pallet.getNumberOfColors();i++){
            int color=pallet.getColor(i);
            colors.add(color & 0xFFFFFF);
        }
    }

    @Override
    protected boolean placeCloseButton() {
        return true;
    }

    @Override
    protected void init() {
        super.init();
        itemHeight= font.lineHeight*2;
        int widgetHeight=21;
        addRenderableWidget(rSlider=new ForgeSlider(
                contentOffsetX+LIST_COLUMN_WIDTH,
                contentOffsetY,
                contentWidth-LIST_COLUMN_WIDTH,
                widgetHeight,
                Component.literal("R : "),
                Component.empty(),
                0,255,0,1,0,true
        ));
        addRenderableWidget(gSlider=new ForgeSlider(
                contentOffsetX+LIST_COLUMN_WIDTH,
                contentOffsetY+widgetHeight,
                contentWidth-LIST_COLUMN_WIDTH,
                widgetHeight,
                Component.literal("G : "),
                Component.empty(),
                0,255,0,1,0,true
        ));
        addRenderableWidget(bSlider=new ForgeSlider(
                contentOffsetX+LIST_COLUMN_WIDTH,
                contentOffsetY+widgetHeight*2,
                contentWidth-LIST_COLUMN_WIDTH,
                widgetHeight,
                Component.literal("B : "),
                Component.empty(),
                0,255,0,1,0,true
        ));
        Component removeText=Component.literal("remove");
        int textWidth=font.width(removeText);
        addRenderableWidget(removeButton=new PlainTextButton(
                contentOffsetX+contentWidth-textWidth-5,
                contentOffsetY+widgetHeight*3+5,
                textWidth,
                font.lineHeight,
                removeText,
                (button)->{
                    if (currentColorIndex!=null && 0<=currentColorIndex &&currentColorIndex<colors.size()){
                        removeColor(currentColorIndex);
                    }
                },
                font
        ));
        rSlider.visible
                =bSlider.visible
                =gSlider.visible
                =removeButton.visible
                =false;
        addRenderableWidget(selectedColorString=new StringWidget(
                contentOffsetX+LIST_COLUMN_WIDTH+6,
                contentOffsetY+widgetHeight*3+5,
                64,
                font.lineHeight,
                Component.empty(),
                font
        ));
    }
    @Override
    public void tick() {
        super.tick();
        if (hovered>=0){
            hoveredTick++;
        }else {
            hoveredTick=0;
        }
        if (rSlider==null||gSlider==null||bSlider==null||selectedColorString==null)return;
        boolean colorSelected=currentColorIndex!=null && 0<=currentColorIndex && currentColorIndex<colors.size();
        if (removeButton!=null) {
            removeButton.visible = rSlider.visible = gSlider.visible = bSlider.visible = colorSelected;
        }
        if (!colorSelected)return;
        currentColor= FastColor.ARGB32.color(
                0,
                rSlider.getValueInt(),
                gSlider.getValueInt(),
                bSlider.getValueInt()
        );
        selectedColorString.setMessage(
                Component.literal(String.format("%d : #%06x",currentColorIndex+1,(0xFF|currentColor)))
        );
        colors.set(currentColorIndex,currentColor);
    }

    @Override
    protected void renderContent(GuiGraphics graphics, int contentOffsetX, int contentOffsetY, int contentWidth, int contentHeight, int mouseX, int mouseY, float partial) {
        boolean selection=false;

        for (var i=0;i<Math.min(colors.size()+1,maxColorCount);i++){
            int color=i<colors.size() ? colors.get(i):0;
            int itemOffsetY=contentOffsetY+itemHeight*i;
            int relativeMouseX=mouseX-contentOffsetX;
            int relativeMouseY=mouseY-itemOffsetY;
            if (contentOffsetX< relativeMouseX && relativeMouseX<contentOffsetX+LIST_COLUMN_WIDTH && 0<relativeMouseY && relativeMouseY<itemHeight){
                selection=true;
                if (hovered!=i) {
                    hovered = i;
                    hoveredTick=0;
                }
                float factor= 1- Mth.square(1-Mth.clamp(hoveredTick+partial,0,6f)/6f);
                graphics.fill(contentOffsetX,itemOffsetY, (int) (contentOffsetX+LIST_COLUMN_WIDTH*factor),itemOffsetY+itemHeight,0x33FFFFFF);
            }
            graphics.drawString(
                    font,
                    i>=colors.size()?"Add New Color":String.format("%2d : #%06x",i+1,color),
                    contentOffsetX+5,
                    itemOffsetY+itemHeight/2-font.lineHeight/2,
                    0xFFFFFFFF
            );
            if (i<colors.size()) {
                graphics.fill(
                        contentOffsetX + LIST_COLUMN_WIDTH - 5 - (itemHeight - 6),
                        itemOffsetY + 3,
                        contentOffsetX + LIST_COLUMN_WIDTH - 5,
                        itemOffsetY + itemHeight - 3,
                        (color & 0xFFFFFF) | 0xFF000000
                );
            }
            graphics.hLine(contentOffsetX,contentOffsetX+LIST_COLUMN_WIDTH,itemOffsetY+itemHeight,0xAAFFFFFF);
        }
        graphics.vLine(contentOffsetX+LIST_COLUMN_WIDTH,contentOffsetY,contentOffsetY+contentHeight,0xFFFFFFFF);

        if (!selection){
            hovered=-1;
        }
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int scroll) {
        if (scroll ==0 && hovered >=0){
            if(hovered<colors.size()) {
                playSelectionSound();
                selectColor(hovered);
                return true;
            }else if (colors.size()<maxColorCount){
                playSelectionSound();
                colors.add(0xFFFFFF);
                selectColor(colors.size()-1);
            }
        }
        return super.mouseClicked(mouseX, mouseY, scroll);
    }

    private void selectColor(int index){
        var color = colors.get(index);
        currentColorIndex = index;
        currentColor = color;
        if (rSlider==null||gSlider==null||bSlider==null)return;
        rSlider.setValue(FastColor.ARGB32.red(color));
        gSlider.setValue(FastColor.ARGB32.green(color));
        bSlider.setValue(FastColor.ARGB32.blue(color));
        if (currentColor!=null && currentColorIndex!=null){
            if (selectedColorString==null)return;
            selectedColorString.setMessage(
                    Component.literal(String.format("%d : #%06x",currentColorIndex+1,(0xFF|currentColor)))
            );
            selectedColorString.setWidth(font.width(selectedColorString.getMessage()));
        }
    }
    private void removeColor(int index){
        colors.remove(index);
        currentColor=currentColorIndex=null;
        if (rSlider==null||gSlider==null||bSlider==null || selectedColorString==null)return;
        rSlider.setValue(0);
        gSlider.setValue(0);
        bSlider.setValue(0);
        selectedColorString.setMessage(Component.empty());
    }

    @Override
    public void onClose() {
        super.onClose();
        int[] colorArray=new int[colors.size()];
        for (var i=0;i<colors.size();i++){
            colorArray[i]=colors.get(i);
        }
        RipplesConfig.setColorPallet(new ColorPallet(colorArray));
    }
}
