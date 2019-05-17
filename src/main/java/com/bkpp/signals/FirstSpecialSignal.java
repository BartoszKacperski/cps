package com.bkpp.signals;

public class FirstSpecialSignal extends SinusoidalSignal {
    @Override
    public Double getValue(Double t){
        double firstValue = 2 * Math.sin(Math.PI * t + Math.PI/2.0);
        double secondValue = 5 * Math.sin(4 * Math.PI * t + Math.PI/2.0);

        return firstValue + secondValue;
    }

    @Override
    public String toString(){
        return "S1";
    }
}
