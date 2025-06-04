package com.alrex.ripples.numerics;

import net.minecraft.util.Mth;

public record ComplexF(float real, float imaginal) {
    public static final ComplexF IMAGINARY_UNIT=new ComplexF(0,1);
    public static final ComplexF ONE=new ComplexF(1,0);
    public static final ComplexF ZERO=new ComplexF(0,0);

    public static ComplexF fromPhase(float length, float phaseRadian){
        return new ComplexF(length*Mth.cos(phaseRadian),length*Mth.sin(phaseRadian));
    }
    public static ComplexF fromPhase(float phaseRadian){
        return fromPhase(1f,phaseRadian);
    }
    public static ComplexF fromReal(float real){
        return new ComplexF(real,0);
    }

    public float magnitude(){
        return Mth.sqrt(this.magnitudeSquared());
    }
    public float magnitudeSquared(){
        return (real*real+imaginal*imaginal);
    }
    public ComplexF add(ComplexF v){
        return new ComplexF(this.real+v.real,this.imaginal+v.imaginal);
    }
    public ComplexF subtract(ComplexF v){
        return new ComplexF(this.real-v.real,this.imaginal-v.imaginal);
    }
    public ComplexF multiply(ComplexF v){
        return new ComplexF(
                this.real*v.real-this.imaginal*v.imaginal,
                this.real*v.imaginal+this.imaginal*v.real
        );
    }
    public ComplexF multiply(float v){
        return new ComplexF(this.real*v,this.imaginal*v);
    }
}
