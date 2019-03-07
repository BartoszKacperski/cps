package com.bkpp.signals;

public class HalfStrightSinusoidalSignal extends Signal {
    @Override
    public Double getValue(Double t) {
        Double value =(2.0 * Math.PI) / super.term * (t - super.startTime);

        Double firstSinus = Math.sin(value);
        Double secondSinus = Math.abs(firstSinus);

        return 0.5 * amplitude * (firstSinus + secondSinus);
    }

    @Override
    public String toString(){
        return "Sygnal sinusoidalny wyprostowany jednopolowkowo";
    }
}
