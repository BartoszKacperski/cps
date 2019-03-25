package com.bkpp.signals;

public class TriangleSignal extends FillFactorPeriodSignal {
    @Override
    public Double getValue(Double t) {
        super.k = (int)Math.floor(((t - startTime)/ period));
        Double rangeStart =  super.k * period + super.startTime;
        Double rangeEnd = super.period * (super.fillFactor + super.k) + startTime;

        if(checkIfNumberInRange(t, rangeStart, rangeEnd)){
            return (amplitude/(fillFactor * period)) * (t - k * period - startTime);
        } else{
            return -amplitude/(period * (1 - fillFactor)) * (t - k * period - startTime) + amplitude/(1 - fillFactor);
        }
    }

    public boolean checkIfNumberInRange(Double t, Double rangeStart, Double rangeEnd){
        return t >= rangeStart && t < rangeEnd;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Sygnal trojkatny").append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }
}
