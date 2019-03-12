package com.bkpp.signals;

import java.util.Random;

public class GaussianNoise extends Signal {

    @Override
    public Double getValue(Double t) {
        Random random = new Random();

        return random.nextGaussian();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Szum gaussowski").append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }
}
