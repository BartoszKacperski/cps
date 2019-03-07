package com.bkpp.signals;

import com.bkpp.Point;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor

public abstract class Signal {
    protected Double frequency = 1.0;
    protected Double amplitude;
    protected Double startTime = 0.0;
    protected Double duration;

    public Signal() {
        amplitude = 0.0;
        startTime = 0.0;
        duration = 0.0;
    }

    public List<Point> getPoints() {
        List<Point> values = new ArrayList<>();

        double currentTime = 0.0;

        for(int i = 0; currentTime < startTime + duration; i++){
            currentTime = (double)i/frequency;
            Point point = new Point(currentTime, getValue(currentTime));
            values.add(point);
        }

        return values;
    }

    public abstract Double getValue(Double t);
}
