package com.alrex.ripples.audio.analyze;

import javax.annotation.Nullable;

public interface WindowFunction {
    public void apply(float[] data);

    class None implements WindowFunction{
        @Override
        public void apply(float[] data) {}
    }

    abstract class CacheableWindowFunction implements WindowFunction{
        @Nullable
        private float[] cachedWindow;

        protected abstract float[] createWindow(int requiredSize);

        protected final float[] getCachedWindow(int requiredSize){
            if (cachedWindow == null || cachedWindow.length!=requiredSize){
                cachedWindow=createWindow(requiredSize);
            }
            return cachedWindow;
        }

        @Override
        public void apply(float[] data) {
            var window=getCachedWindow(data.length);
            for(int i=0;i<data.length;i++){
                data[i]*=window[i];
            }
        }
    }

    class Hamming extends CacheableWindowFunction{
        @Override
        protected float[] createWindow(int requiredSize) {
            var window=new float[requiredSize];
            for(int i=0;i<requiredSize;i++){
                window[i]=(float)(0.5 - 0.5 * Math.cos(2.*Math.PI * (i/ (double) requiredSize)));
            }
            return window;
        }
    }
}
