package com.alrex.ripples.audio.analyze;

import com.alrex.ripples.numerics.BitOperations;
import com.alrex.ripples.numerics.ComplexF;
import net.minecraft.util.Mth;

public class FFT {

    public static ComplexF[] forward(float[] data){
        if (data.length == 0){
            return new ComplexF[0];
        }
        if (data.length == 1){
            return new ComplexF[]{ComplexF.fromReal(data[0])};
        }
        if (!Mth.isPowerOfTwo(data.length)) throw new IllegalArgumentException("Data length have to be pow of 2");
        int stageCount=Mth.log2(data.length);
        int groupSize= data.length;
        int groupCount=1;
        ComplexF[] temp=new ComplexF[data.length];
        ComplexF[] tempTo=new ComplexF[data.length];

        for(int i=0;i< data.length;i++){
            temp[i]=ComplexF.fromReal(data[i]);
        }

        for(var stage=0;stage<stageCount;stage++){

            for (var group=0;group<groupCount;group++){

                int maxCount=groupSize >> 1;
                int offset=group*groupSize;
                ComplexF rotatorBase=ComplexF.fromPhase((float) (-2d*Math.PI/groupSize));
                ComplexF rotator=ComplexF.ONE;
                for (var i=0;i<maxCount;i++){
                    tempTo[offset+i]=temp[offset+i].add(temp[offset+i+maxCount]);
                    tempTo[offset+i+maxCount]=(temp[offset+i].subtract(temp[offset+i+maxCount])).multiply(rotator);
                    rotator=rotator.multiply(rotatorBase);
                }

            }

            var t=temp;
            temp=tempTo;
            tempTo=t;

            groupCount = groupCount << 1;
            groupSize = groupSize >> 1;
        }
        ComplexF[] result=new ComplexF[data.length/2];

        // coefficient for normalize
        float scale= 1f / (data.length >> 1);

        for(var i=0;i<result.length;i++){
            result[i] = temp[BitOperations.bitOrderReverse(i,stageCount)].multiply(scale);
        }
        return result;
    }
    public static float[] power(float[] data){
        var fft=forward(data);
        var power=new float[fft.length];
        for(var i=0;i<fft.length;i++){
            power[i]=fft[i].magnitudeSquared();
        }
        return power;
    }
    public static float[] magnitude(float[] data){
        var fft=forward(data);
        var magnitude=new float[fft.length];
        for(var i=0;i<fft.length;i++){
            magnitude[i]=fft[i].magnitude();
        }
        return magnitude;
    }
    public static float getFrequencyResolution(float sampleRate,int dataLength){
        return sampleRate / dataLength;
    }
}
