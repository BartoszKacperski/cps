package com.bkpp.signals;

public class StrightSinusoidalSignal extends Signal {
    @Override
    public Double getValue(Double t) {
        Double value =(2.0 * Math.PI) / super.term * (t - super.startTime);

        return amplitude * Math.abs(Math.sin(value));
    }

    @Override
    public String toString(){
        return "Sygnal sinusoidalny wyprostowany dwupolowkowo";
    }
}
