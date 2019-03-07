package com.bkpp.signals;

public class RectangleSignal extends FillFactorSignal {
    @Override
    public Double getValue(Double t) {
        super.k = t.intValue();
        Double rangeStart =  super.k * term + super.startTime;
        Double rangeEnd = super.term * (super.fillFactor + super.k) + startTime;

        if(checkIfNumberInRange(t, rangeStart, rangeEnd)){
            return super.amplitude;
        } else{
            return 0.0;
        }
    }

    public boolean checkIfNumberInRange(Double t, Double rangeStart, Double rangeEnd){
        return t >= rangeStart && t < rangeEnd;
    }

    @Override
    public String toString(){
        return "Sygnal prostokatny";
    }
}
