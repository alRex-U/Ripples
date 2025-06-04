package com.alrex.ripples.audio.analyze;

import com.alrex.ripples.audio.AudioManager;
import net.minecraft.util.Mth;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SignalReSampler {
    private static final float BYTE_TO_SHORT_SCALE=(Short.MAX_VALUE*0.95f)/Byte.MAX_VALUE;
    public static short[] getAsMonoChannelSignalArray(AudioFormat format, ByteBuffer buffer){
        int channel =format.getChannels();
        int sampleSizeInBytes=format.getSampleSizeInBits()/8;
        int oneSampleStrideInBytes=channel*sampleSizeInBytes;
        if (sampleSizeInBytes > 2 || sampleSizeInBytes <0)return new short[0];
        if (channel > 2 || channel <0)return new short[0];
        buffer.order(format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);

        short[] data=new short[buffer.remaining()/oneSampleStrideInBytes];
        for(var i=0;i< data.length;i++){
            float sample=sampleSizeInBytes == 2
                    ? buffer.getShort()
                    : buffer.get()*BYTE_TO_SHORT_SCALE;
            if (channel == 2){
                sample += sampleSizeInBytes == 2
                        ? buffer.getShort()
                        : buffer.get()*BYTE_TO_SHORT_SCALE;
                sample/=2f;
            }
            data[i]=(short) sample;
        }
        return data;
    }
    public static List<short[]> getAsMonoChannelSignalSequenceForEveryGameTick(AudioFormat format, ByteBuffer buffer, int resultSingleBlockSize, int resultSampleRate){
        final int gameTickRateInSecond=20;
        int channel =format.getChannels();
        int sampleSizeInBytes=format.getSampleSizeInBits()/8;
        int oneSampleStrideInBytes=channel*sampleSizeInBytes;
        if (sampleSizeInBytes > 2 || sampleSizeInBytes <0)return Collections.emptyList();
        if (channel > 2 || channel <0)return Collections.emptyList();
        buffer.order(format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);

        int sampleCountInOneTick= (int) (format.getSampleRate()/gameTickRateInSecond);
        short[] temp=new short[sampleCountInOneTick];

        int sampleCount=buffer.remaining()/oneSampleStrideInBytes;
        int dataCount=sampleCount /sampleCountInOneTick;
        var dataSequence=new ArrayList<short[]>(dataCount);

        for(var i=0;i<dataCount;i++){
            for(var j=0;j<temp.length;j++){
                float sample=sampleSizeInBytes == 2
                        ? buffer.getShort()
                        : buffer.get()*BYTE_TO_SHORT_SCALE;
                if (channel == 2){
                    sample += sampleSizeInBytes == 2
                            ? buffer.getShort()
                            : buffer.get()*BYTE_TO_SHORT_SCALE;
                    sample/=2f;
                }
                temp[j]=(short) sample;
            }
            dataSequence.add(
                    resample(
                            temp,
                            (int) format.getSampleRate(),
                            resultSampleRate,
                            resultSingleBlockSize
                    )
            );
        }
        return dataSequence;
    }

    public static short[] resample(short[] original,int originalSampleRate,int resampleRate, int maxSize){
        double timeDuration=original.length / (double)originalSampleRate;
        int resampledSize= (int) Math.ceil(timeDuration*resampleRate);
        short[] resampled=new short[Math.min(resampledSize,maxSize)];
        var sampleIndexRatio=originalSampleRate /(float)resampleRate;
        for(int i=0;i<resampled.length;i++){
            float newIndex= i * sampleIndexRatio;
            int j= (int) newIndex;
            float partial=newIndex-j;
            if (j+1 < original.length){
                resampled[i]= (short) Mth.lerp(partial,original[j],original[j+1]);
            }else if (j < original.length){
                resampled[i] = original[j];
            }else {
                resampled[i] = 0;
            }
        }
        return resampled;
    }

    public static float[] decreaseSize(float[] fft,int desiredSize){
        if (desiredSize > fft.length)return fft;
        var result=new float[desiredSize];
        var oneBlockSize=fft.length/(float)desiredSize;
        var window=new float[(int) (oneBlockSize*2f)];
        {
            var windowSum=0f;
            for (var i = 0; i < window.length; i++) {
                windowSum+=window[i] = (float) (0.5 - 0.5 * Math.cos(2.*Math.PI *(i+1)/(window.length+2)));
            }
            for (var i = 0; i < window.length; i++) {
                window[i]/=windowSum;
            }
        }
        for(var i=0;i<result.length;i++){
            int baseIndex= (int) (oneBlockSize*(i+0.5));
            for(var j=0;j<window.length;j++){
                int index=(int) (baseIndex-oneBlockSize+j);
                if (index < 0)continue;
                if (index >= fft.length)break;
                result[i]+=window[j]*fft[index];
            }
        }
        return result;
    }
}
