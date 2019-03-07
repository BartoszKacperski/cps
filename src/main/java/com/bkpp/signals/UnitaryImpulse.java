package com.bkpp.signals;

public class UnitaryImpulse extends ExtendedNoise {

    @Override
    public Double getValue(Double t) {
        if(Math.abs(t - parameter) < 1E-10){
            return 1.0;
        } else {
            return 0.0;
        }
    }

    @Override
    public String toString(){
        return "Impuls jednoskokowy";
    }
}
