package com.bkpp.signals;


import java.util.List;

public class FilterSignal extends Signal {

    public FilterSignal(List<Point> pointList) {
        super.setPoints(pointList);
    }

    @Override
    public Double getValue(Double t) {
        return null;
    }

    @Override
    public String toString(){
        return "Filtr";
    }
}
