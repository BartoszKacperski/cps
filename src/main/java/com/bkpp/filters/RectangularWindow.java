package com.bkpp.filters;

public class RectangularWindow implements Window {
    @Override
    public double getValue(double x, double M) {
        if((M - 1)/2 < x && x < -(M - 1)/2){
            return 0.0;
        }

        return 1.0;
    }

    @Override
    public String toString(){
        return "Okno prostokątne";
    }
}
