package com.alrex.ripples.numerics;

public class BitOperations {
    public static int bitOrderReverse(int i,int digit){
        if (digit > 32)return Integer.reverse(i);
        if (digit <= 0)return 0;
        i=Integer.reverse(i);
        return i >>> (32 - digit);
    }
}
