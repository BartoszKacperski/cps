package com.bkpp.signals;

public class SinusoidalContinuousSignal extends ContinuousSignal {
    @Override
    public Double getValue(Double t) {
        Double value =(2.0 * Math.PI) / super.term * (t - super.startTime);

        return super.amplitude * Math.sin(value);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Sygnal sinusoidalny").append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }
}
