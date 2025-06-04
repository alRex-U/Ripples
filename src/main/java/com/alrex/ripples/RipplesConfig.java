package com.alrex.ripples;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = Ripples.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RipplesConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec SPEC;

    static {
        SPEC = BUILDER.build();
    }

    public void register(ModLoadingContext context){
        context.registerConfig(ModConfig.Type.CLIENT,SPEC);
    }
}
