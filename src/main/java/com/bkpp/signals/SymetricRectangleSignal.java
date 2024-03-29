package com.bkpp.signals;

public class SymetricRectangleSignal extends FillFactorTermSignal {
    @Override
    public Double getValue(Double t) {
        super.k = (int)Math.floor(((t - startTime)/ term));
        Double rangeStart =  super.k * term + super.startTime;
        Double rangeEnd = super.term * (super.fillFactor + super.k) + startTime;

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
