package com.bkpp.signals;


import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor

public class UnvaryingNoise extends Signal {

    @Override
    public Double getValue(Double t) {
        return ThreadLocalRandom.current().nextDouble(-super.amplitude, super.amplitude);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Szum o rozkładzie jednostajnym").append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }

}
