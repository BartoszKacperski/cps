package com.bkpp.signals;

public class TriangleSignal extends FillFactorTermSignal {
    @Override
    public Double getValue(Double t) {
        super.k = (int)(t/term);
        Double rangeStart =  super.k * term + super.startTime;
        Double rangeEnd = super.term * (super.fillFactor + super.k) + startTime;

        if(checkIfNumberInRange(t, rangeStart, rangeEnd)){
            return (amplitude/(fillFactor * term)) * (t - k * term - startTime);
        } else{
            return -amplitude/(term * (1 - fillFactor)) * (t - k * term - startTime) + amplitude/(1 - fillFactor);
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
