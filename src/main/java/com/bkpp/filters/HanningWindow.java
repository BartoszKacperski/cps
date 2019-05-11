package com.bkpp.filters;

public class HanningWindow implements Window {
    @Override
    public double getValue(double x, double M) {
        return 0.5 - 0.5 * Math.cos((2.0 * Math.PI * x)/M);
    }

    @Override
    public String toString(){
        return "Okno Hanninga";
    }
}
