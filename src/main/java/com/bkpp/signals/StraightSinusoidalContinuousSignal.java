package com.bkpp.signals;

public class StraightSinusoidalContinuousSignal extends ContinuousSignal {
    @Override
    public Double getValue(Double t) {
        double value =(2.0 * Math.PI) / super.term * (t - super.startTime);

        return amplitude * Math.abs(Math.sin(value));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Sygnal sinusoidalny wyprostowany dwupolowkowo").append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }
}
