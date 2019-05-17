package com.bkpp.signals;

public class ThirdSpecialSignal extends SinusoidalSignal {
    @Override
    public Double getValue(Double t){
        double firstValue = 5 * Math.sin(Math.PI * t);
        double secondValue = Math.sin(8 * Math.PI * t);

        return firstValue + secondValue;
    }

    @Override
    public String toString(){
        return "S3";
    }
}
