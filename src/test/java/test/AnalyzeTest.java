package test;

import com.alrex.ripples.audio.analyze.FFT;
import com.alrex.ripples.audio.analyze.SignalReSampler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;

import java.io.IOException;

public class AnalyzeTest {
    @Test
    public void testFFT(){
        var chart=new XYChart(800,600);
        var data=new float[2048];
        for(int i=0;i< data.length;i++){
            data[i]=(float) Math.cos(2*Math.PI*i*(20/2048d));
            data[i]+=0.5f*(float) Math.cos(2*Math.PI*i*(100/2048d));
            data[i]+=0.3f*(float) Math.cos(2*Math.PI*i*(220/2048d));
        }
        chart.addSeries("data",data);
        try {
            BitmapEncoder.saveBitmap(chart, "temp/data.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var fftChart=new XYChart(800,600);
        var fft= FFT.magnitude(data);
        fftChart.addSeries("fft",fft);
        try {
            BitmapEncoder.saveBitmap(fftChart, "temp/fft.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testResampling(){
        var chart=new XYChart(800,600);
        var data=new short[4096];
        short scale=Short.MAX_VALUE/2;
        for(int i=0;i< data.length;i++){
            data[i]= (short) (scale * Math.cos(2*Math.PI*i*(40/4096d)));
            data[i]+= (short) (scale*0.5f* Math.cos(2*Math.PI*i*(200/4096d)));
            data[i]+= (short) (scale*0.3f* Math.cos(2*Math.PI*i*(440/4096d)));
        }
        {
            var temp=new int[data.length];
            for(var i=0;i<temp.length;i++){
                temp[i]=data[i];
            }
            chart.addSeries("data",temp);
        }
        try {
            BitmapEncoder.saveBitmap(chart, "temp/data.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var resampleChart=new XYChart(800,600);
        var resample= SignalReSampler.resample(data,88200,44100,Integer.MAX_VALUE);
        Assertions.assertEquals(2048,resample.length);
        {
            var temp=new int[resample.length];
            for(var i=0;i<temp.length;i++){
                temp[i]=resample[i];
            }
            resampleChart.addSeries("resampled",temp);
        }
        try {
            BitmapEncoder.saveBitmap(resampleChart, "temp/data_resampled.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var fftOriginalData=new float[data.length];
        for(var i=0;i< fftOriginalData.length;i++){
            fftOriginalData[i]=data[i]/(float)Short.MAX_VALUE;
        }
        var fftOriginalChart=new XYChart(800,600);
        var fftOriginal= FFT.magnitude(fftOriginalData);
        fftOriginalChart.addSeries("fft_Original",fftOriginal);
        try {
            BitmapEncoder.saveBitmap(fftOriginalChart, "temp/fft.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var fftData=new float[resample.length];
        for(var i=0;i< fftData.length;i++){
            fftData[i]=resample[i]/(float)Short.MAX_VALUE;
        }
        var fftChart=new XYChart(800,600);
        var fft= FFT.magnitude(fftData);
        fftChart.addSeries("fft",fft);
        try {
            BitmapEncoder.saveBitmap(fftChart, "temp/fft_resampled.png", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
