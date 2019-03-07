package com.bkpp.signals;

import java.util.concurrent.ThreadLocalRandom;

public class ImpulseNoise extends ExtendedNoise {

    @Override
    public Double getValue(Double t) {
        Double random = ThreadLocalRandom.current().nextDouble();

        if(random <= parameter){
            return amplitude;
        } else {
            return 0.0;
        }
    }

    @Override
    public String toString(){
        return "Szum impulsowy";
    }
}
