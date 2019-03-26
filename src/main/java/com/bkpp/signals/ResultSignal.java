package com.bkpp.signals;

import com.bkpp.Point;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor

public class ResultSignal extends Signal {

    public ResultSignal(List<Point> pointList) {
        this.setPoints(pointList);
    }

    @Override
    public Double getValue(Double t) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public String toString(){
        return "Sygnal wynikowy";
    }
}
