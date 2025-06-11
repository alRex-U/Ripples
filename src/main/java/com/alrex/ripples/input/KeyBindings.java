package com.alrex.ripples.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jline.keymap.KeyMap;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class KeyBindings {
    private static final KeyMapping openSettingKey=new KeyMapping("ripples.key.open_setting", KeyConflictContext.IN_GAME, KeyModifier.ALT, InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_R,"key.categories.ripples");

    public static KeyMapping getOpenSettingKey() {
        return openSettingKey;
    }

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event){
        event.register(openSettingKey);
    }
}
