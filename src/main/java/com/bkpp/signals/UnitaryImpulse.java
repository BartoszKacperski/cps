package com.bkpp.signals;

public class UnitaryImpulse extends DiscreteSignal {

    @Override
    public Double getValue(Double t) {
        double sample = (t - super.startTime) * super.frequency;
        if(Math.abs(sample - parameter) < 1E-10){
            return 1.0;
        } else {
            return 0.0;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Impuls jednostkowy").append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }
}
