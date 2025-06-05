package com.alrex.ripples;

import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.api.gui.SpectrumStyle;
import com.alrex.ripples.render.hud.spectrum.HotbarSpectrum;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = Ripples.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RipplesConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<String> SPECTRUM;
    public static final ForgeConfigSpec.DoubleValue CLIP_FT_SIZE;
    public static final ForgeConfigSpec.DoubleValue SPECTRUM_OPACITY;
    public static final ForgeConfigSpec.EnumValue<SpectrumStyle> SPECTRUM_STYLE;

    private static final ForgeConfigSpec SPEC;

    public static ResourceLocation getSpectrumID(){
        var id=SPECTRUM.get();
        return new ResourceLocation(id);
    }
    public static void setSpectrumID(ResourceLocation id){
        SPECTRUM.set(id.toString());
    }

    static {
        BUILDER.comment(
                "Used Spectrum HUD type",
                "Ripples provides ...",
                "----------"
        );

        RipplesSpectrumRegistry.get()
                .getRegisteredEntries()
                .stream()
                .map(ResourceLocation::toString)
                .forEach(BUILDER::comment);
        BUILDER.comment(
                "----------",
                "But other mods added more type"
        );
        SPECTRUM= BUILDER.define("spectrum", HotbarSpectrum.SPECTRUM_ID.toString());

        SPECTRUM_STYLE=BUILDER
                .comment("Render style of spectrum")
                .defineEnum("spectrum_style",SpectrumStyle.BLOCKS);

        SPECTRUM_OPACITY=BUILDER
                .comment()
                .defineInRange("spectrum_opactity",0.4,0,1.);

        CLIP_FT_SIZE=BUILDER
                .comment("Amount of analysis results to be output")
                .defineInRange("output_amount",1d,0.1,1d);

        SPEC = BUILDER.build();
    }

    public static void register(ModLoadingContext context){
        context.registerConfig(ModConfig.Type.CLIENT,SPEC);
    }
}
