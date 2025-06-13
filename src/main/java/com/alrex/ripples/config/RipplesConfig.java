package com.alrex.ripples.config;

import com.alrex.ripples.Ripples;
import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.api.gui.SpectrumStyle;
import com.alrex.ripples.render.RenderContent;
import com.alrex.ripples.api.gui.ColorPallet;
import com.alrex.ripples.render.hud.soundmap.CircleSoundMap;
import com.alrex.ripples.render.hud.spectrum.HotbarSpectrum;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.Nullable;
import java.util.*;

@OnlyIn(Dist.CLIENT)
public class RipplesConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.EnumValue<RenderContent> CONTENT_TYPE;

    public static final ForgeConfigSpec.ConfigValue<String> SPECTRUM;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> COLOR_PALLET;
    public static final ForgeConfigSpec.DoubleValue CLIP_FT_SIZE;
    public static final ForgeConfigSpec.DoubleValue DOWN_SAMPLING_RATE;
    public static final ForgeConfigSpec.DoubleValue SPECTRUM_OPACITY;
    public static final ForgeConfigSpec.DoubleValue SPECTRUM_GAIN;
    public static final ForgeConfigSpec.EnumValue<SpectrumStyle> SPECTRUM_STYLE;

    public static final ForgeConfigSpec.ConfigValue<String> SOUND_MAP;
    public static final ForgeConfigSpec.DoubleValue SOUND_MAP_OPACITY;
    public static final ForgeConfigSpec.DoubleValue SOUND_MAP_GAIN;

    public static final ForgeConfigSpec.BooleanValue SHOW_MUSIC_TITLE;

    public static final ForgeConfigSpec.BooleanValue TAKE_INTO_ACCOUNT_PITCH;

    @Nullable
    private static ColorPallet cachedPallet;

    private static void updateColorPalletCache(){
        var list=COLOR_PALLET.get();
        var colors=new LinkedList<Integer>();
        try{
            for (var item:list){
                colors.addLast(Integer.parseInt(item,16));
            }
        }catch (NumberFormatException e){
            cachedPallet=new ColorPallet();
        }
        var colorValues=new int[colors.size()];
        for(var i=0;i<colorValues.length;i++){
            colorValues[i]= Objects.requireNonNull(colors.pollFirst());
        }
        cachedPallet=new ColorPallet(colorValues);
    }

    public static ColorPallet getColorPallet() {
        if (cachedPallet==null){
            updateColorPalletCache();
        }
        return cachedPallet;
    }

    public static void setColorPallet(ColorPallet pallet){
        var colors=new String[pallet.getNumberOfColors()];
        for(var i=0;i<colors.length;i++){
            colors[i]=Integer.toHexString(pallet.getColor(i));
        }
        COLOR_PALLET.set(Arrays.asList(colors));
        updateColorPalletCache();
    }

    private static final ForgeConfigSpec SPEC;

    public static ResourceLocation getSpectrumID(){
        var id=SPECTRUM.get();
        return new ResourceLocation(id);
    }
    public static void setSpectrumID(ResourceLocation id){
        SPECTRUM.set(id.toString());
    }
    public static ResourceLocation getSoundMapID(){
        return new ResourceLocation(SOUND_MAP.get());
    }
    public static void setSoundMapID(ResourceLocation id){
        SOUND_MAP.set(id.toString());
    }

    static {
        CONTENT_TYPE=BUILDER.defineEnum("content_type",RenderContent.SPECTRUM);
        BUILDER.push("spectrum");
        {
            BUILDER.comment(
                    "Used Spectrum HUD type",
                    "Ripples provides ...",
                    "----------"
            );
            RipplesSpectrumRegistry.get()
                    .getRegisteredSpectrumIDs()
                    .stream()
                    .map(ResourceLocation::toString)
                    .forEach(BUILDER::comment);
            BUILDER.comment(
                    "----------",
                    "But other mods added more type"
            );
            SPECTRUM = BUILDER.define("spectrum", HotbarSpectrum.SPECTRUM_ID.toString());

            SPECTRUM_STYLE = BUILDER
                    .comment("Render style of spectrum")
                    .defineEnum("spectrum_style", SpectrumStyle.BLOCKS);

            SPECTRUM_OPACITY = BUILDER
                    .comment()
                    .defineInRange("spectrum_opacity", 0.4, 0, 1.);

            SPECTRUM_GAIN = BUILDER
                    .comment()
                    .defineInRange("spectrum_gain", 1d, 0d, 10000d);

            CLIP_FT_SIZE = BUILDER
                    .comment("Amount of analysis results to be output")
                    .defineInRange("output_amount", 0.5d, 0.1, 1d);

            DOWN_SAMPLING_RATE=BUILDER
                    .comment("Down sampling rate of spectrum data")
                    .defineInRange("output_down_sampling_rate",0.2,0.1,1.);
        }
        BUILDER.pop();
        BUILDER.push("sound_map");
        {
            BUILDER.comment(
                    "Used Sound Map HUD type",
                    "Ripples provides ...",
                    "----------"
            );
            RipplesSpectrumRegistry.get()
                    .getRegisteredSoundMapIDs()
                    .stream()
                    .map(ResourceLocation::toString)
                    .forEach(BUILDER::comment);
            BUILDER.comment(
                    "----------",
                    "But other mods added more type"
            );
            SOUND_MAP=BUILDER
                    .define("sound_map", CircleSoundMap.SOUND_MAP_ID.toString());
            SOUND_MAP_GAIN=BUILDER
                    .defineInRange("sound_map_gain",1d,0,10000d);
            SOUND_MAP_OPACITY=BUILDER
                    .defineInRange("sound_map_opacity",0.4,0d,1d);
        }
        BUILDER.pop();

        TAKE_INTO_ACCOUNT_PITCH=BUILDER
                .comment(
                        "Calculate spectrum with sound pitch",
                        "This can increase calculation cost"
                )
                .define("take_into_account_pitch",true);
        COLOR_PALLET = BUILDER
                .comment(
                        "Used Color List, as 24 bit rgb integer by hex string",
                        "For example \"FFFFFF\" for white"
                )
                .defineList(
                        "colors", Arrays.asList("76FFFF", "00FFFF", "00FF7b"),
                        (Object e) -> {
                            try {
                                Integer.parseInt(e.toString(), 16);
                            } catch (NumberFormatException exception) {
                                return false;
                            }
                            return true;
                        }
                );
        SHOW_MUSIC_TITLE=BUILDER
                .define("show_music_title",true);
        SPEC = BUILDER.build();
    }

    public static void register(ModLoadingContext context){
        context.registerConfig(ModConfig.Type.CLIENT,SPEC);
    }
}
