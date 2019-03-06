package com.bkpp.signals;


import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor

public class UnvaryingNoise extends Noise {

    @Override
    public Double getValue(Double t) {
        return ThreadLocalRandom.current().nextDouble(-super.amplitude, super.amplitude);
    }

    @Override
    public String toString() {
        return "Szum o rozkladzie jednostajnym";
    }

}
