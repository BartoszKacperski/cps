package com.bkpp.signals;

public class StraightSinusoidalContinuousSignal extends ContinuousSignal {
    @Override
    public Double getValue(Double t) {
        double value =(2.0 * Math.PI) / super.term * (t - super.startTime);

        return amplitude * Math.abs(Math.sin(value));
    }

    @Override
    public String toString(){
        return "Sygnal sinusoidalny wyprostowany dwupolowkowo";
    }
}
