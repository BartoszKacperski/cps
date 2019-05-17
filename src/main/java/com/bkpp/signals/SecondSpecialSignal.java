package com.bkpp.signals;

public class SecondSpecialSignal extends SinusoidalSignal {
    @Override
    public Double getValue(Double t){
        double firstValue = 2 * Math.sin(Math.PI * t);
        double secondValue = Math.sin(2 * Math.PI * t);
        double thirdValue = 5 * Math.sin(4 * Math.PI * t);

        return firstValue + secondValue + thirdValue;
    }

    @Override
    public String toString(){
        return "S2";
    }
}
