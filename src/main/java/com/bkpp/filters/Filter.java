package com.bkpp.filters;

public interface Filter {
    double filterValue(int i);
    double frequency(double fp, double f0);
    double suitResult(double amplitude, double y);
}
