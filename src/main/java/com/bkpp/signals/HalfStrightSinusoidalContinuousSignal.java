package com.bkpp.signals;

public class HalfStrightSinusoidalContinuousSignal extends ContinuousSignal {
    @Override
    public Double getValue(Double t) {
        Double value =(2.0 * Math.PI) / super.term * (t - super.startTime);

        Double firstSinus = Math.sin(value);
        Double secondSinus = Math.abs(firstSinus);

        return 0.5 * amplitude * (firstSinus + secondSinus);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Sygnal sinusoidalny wyprostowany jednopolowkowo").append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }
}
