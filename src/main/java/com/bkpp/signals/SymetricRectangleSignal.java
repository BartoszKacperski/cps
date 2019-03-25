package com.bkpp.signals;

public class SymetricRectangleSignal extends FillFactorPeriodSignal {
    @Override
    public Double getValue(Double t) {
        super.k = (int)Math.floor(((t - startTime)/ period));
        Double rangeStart =  super.k * period + super.startTime;
        Double rangeEnd = super.period * (super.fillFactor + super.k) + startTime;

        if(checkIfNumberInRange(t, rangeStart, rangeEnd)){
            return super.amplitude;
        } else{
            return -super.amplitude;
        }
    }

    public boolean checkIfNumberInRange(Double t, Double rangeStart, Double rangeEnd){
        return t >= rangeStart && t < rangeEnd;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Sygnal prostokatny symetryczny").append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }
}
