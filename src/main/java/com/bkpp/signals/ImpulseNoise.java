package com.bkpp.signals;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@NoArgsConstructor

public class ImpulseNoise extends Signal {
    private Double probability;

    @Override
    public Double getValue(Double t) {
        Double random = ThreadLocalRandom.current().nextDouble();

        if(random <= probability){
            return amplitude;
        } else {
            return 0.0;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Szum impulsowy").append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }

}
