package com.bkpp.signals;

import com.bkpp.Point;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor

public abstract class Noise {
    protected Double sampling = 0.05;
    protected Double amplitude;
    protected Double startTime;
    protected Double duration;

    public Noise(){
        amplitude = 0.0;
        startTime = 0.0;
        duration = 0.0;
    }

    public List<Point> getPoints() {
        List<Point> values = new ArrayList<>();

        for (double t = startTime; t <= duration; t += sampling) {
            values.add(new Point(t, getValue(t)));
        }

        return values;
    }

    public abstract Double getValue(Double t);
}
