package com.bkpp.signals;

import java.util.Random;

public class GaussianNoise extends Signal {

    @Override
    public Double getValue(Double t) {
        Random random = new Random();

        return random.nextGaussian();
    }

    @Override
    public String toString(){
        return "Szum gaussowski";
    }
}
