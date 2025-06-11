package com.alrex.ripples.audio.processors;

import com.alrex.ripples.api.RipplesSpectrumRegistry;
import com.alrex.ripples.api.audio.RelativeAngleInFOV;
import com.alrex.ripples.api.gui.AbstractSoundMapRenderer;
import com.alrex.ripples.api.audio.SoundMapSource;
import com.alrex.ripples.audio.IAudioWaveProvider;
import com.alrex.ripples.audio.IAudioProcessor;
import com.alrex.ripples.audio.analyze.SignalReSampler;
import com.alrex.ripples.config.RipplesConfig;
import com.alrex.ripples.util.CameraUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

@OnlyIn(Dist.CLIENT)
public class SoundMapAudioProcessor implements IAudioProcessor {
    @Nullable
    private AbstractSoundMapRenderer renderer;

    public AbstractSoundMapRenderer getRenderer() {
        if (renderer==null){
            renderer= RipplesSpectrumRegistry.get().getSoundMap(RipplesConfig.getSoundMapID());
        }
        return renderer;
    }

    private HashMap<IAudioWaveProvider, SoundMapSource> previousTickProviderToSoundInfo=new HashMap<>(128);
    private HashMap<IAudioWaveProvider, SoundMapSource> currentProviderToSoundInfo=new HashMap<>(128);
    private final HashSet<IAudioWaveProvider> accessibleProviders=new HashSet<>(128);

    @Override
    public void tick(Collection<IAudioWaveProvider> providers) {
        if (Minecraft.getInstance().player==null)return;
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
                    ? CameraUtil.getRelativeAngleOfPointSeenFromCamera(camera,soundPos,2)
                    : null;
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
