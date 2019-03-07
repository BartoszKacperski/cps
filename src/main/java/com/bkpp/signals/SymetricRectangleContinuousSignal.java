package com.bkpp.signals;

public class SymetricRectangleContinuousSignal extends FillFactorContinuousSignal {
    @Override
    public Double getValue(Double t) {
        super.k = t.intValue();
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
    public String toString(){
        return "Sygnal prostokatny symetryczny";
    }
}
