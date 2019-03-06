package com.bkpp.signals;

public class SinusoidalSignal extends Signal {
    @Override
    public Double getValue(Double t) {
        Double value =(2.0 * Math.PI) / super.term * (t - super.startTime);

        return Math.sin(super.amplitude * value);
    }

    @Override
    public String toString(){
        return "Sygnal sinusoidalny";
    }
}
