package com.bkpp.filters;

public class HighPassFilter implements Filter {
    @Override
    public double filterValue(int i) {
        return Math.pow(-1.0, i);
    }

    @Override
    public double frequency(double fp, double f0) {
        return fp/2.0 - f0;
    }

    @Override
    public double suitResult(double amplitude, double y) {
        return y * amplitude;
    }

    @Override
    public String toString(){
        return "Filtr górnoprzepustowy";
    }
}
