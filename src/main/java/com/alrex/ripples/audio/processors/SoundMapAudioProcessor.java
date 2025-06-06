package com.alrex.ripples.audio.processors;

import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.api.audio.RelativeAngleInFOV;
import com.alrex.ripples.api.gui.AbstractSoundMapRenderer;
import com.alrex.ripples.api.audio.SoundMapSource;
import com.alrex.ripples.audio.AudioWaveProvider;
import com.alrex.ripples.audio.IAudioProcessor;
import com.alrex.ripples.audio.analyze.SignalReSampler;
import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.util.CameraUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class SoundMapAudioProcessor implements IAudioProcessor {
    @Nullable
    private AbstractSoundMapRenderer renderer;

    public AbstractSoundMapRenderer getRenderer() {
        if (renderer==null){
            renderer= RipplesSpectrumRegistry.get().getSoundMap(RipplesConfig.getSoundMapID());
        }
        return renderer;
    }

    private HashMap<AudioWaveProvider, SoundMapSource> previousTickProviderToSoundInfo=new HashMap<>(128);
    private HashMap<AudioWaveProvider, SoundMapSource> currentProviderToSoundInfo=new HashMap<>(128);
    private final HashSet<AudioWaveProvider> accessibleProviders=new HashSet<>(128);

    @Override
    public void tick(Collection<AudioWaveProvider> providers) {
        {
            var t=previousTickProviderToSoundInfo;
            previousTickProviderToSoundInfo=currentProviderToSoundInfo;
            currentProviderToSoundInfo=t;

            currentProviderToSoundInfo.clear();
            accessibleProviders.clear();
        }

        var camera=Minecraft.getInstance().gameRenderer.getMainCamera();
        var listenerPos= camera.getPosition();
        for(var provider:providers){
            provider.tick();
            var data=provider.getCurrentWave();
            if (data==null)continue;
            float soundPressure=provider.getGainFor(listenerPos)* SignalReSampler.getSoundPressure(data);
            var soundPos=provider.getPosition();
            var angle = soundPos!=null && !soundPos.equals(Vec3.ZERO)
                    ? CameraUtil.getRelativeAngleOfPointSeenFromCamera(camera,soundPos)
                    : RelativeAngleInFOV.EXACT_FRONT;
            var info=new SoundMapSource(soundPressure,angle);

            currentProviderToSoundInfo.put(provider,info);
        }
        accessibleProviders.addAll(previousTickProviderToSoundInfo.keySet());
        accessibleProviders.addAll(currentProviderToSoundInfo.keySet());
    }

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partial, int width, int height) {
        var infos=accessibleProviders.stream()
                .map(it-> SoundMapSource.lerp(partial,previousTickProviderToSoundInfo.get(it),currentProviderToSoundInfo.get(it)))
                .toList();
        getRenderer().render(forgeGui,guiGraphics,infos,partial,width,height);
    }

    @Override
    public void notifyConfigUpdated() {
        renderer= RipplesSpectrumRegistry.get().getSoundMap(RipplesConfig.getSoundMapID());
    }
}
