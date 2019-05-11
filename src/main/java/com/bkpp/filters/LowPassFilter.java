package com.bkpp.filters;

public class LowPassFilter implements Filter {
    @Override
    public double filterValue(int i) {
        return 1;
    }

    @Override
    public double frequency(double fp, double f0) {
        return f0;
    }

    @Override
    public double suitResult(double amplitude, double y) {
        return y;
    }

    @Override
    public String toString(){
        return "Filtr dolnoprzepustowy";
    }
}
