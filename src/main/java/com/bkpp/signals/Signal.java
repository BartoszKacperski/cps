package com.bkpp.signals;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor

public abstract class Signal implements Serializable {
    protected Double frequency = 1.0;
    protected Double amplitude = 0.0;
    protected Double startTime = 0.0;
    protected Double duration = 0.0;
    private List<Point> points;


    public void computePoints(){
        points = new ArrayList<>();

        double currentTime = 0.0;

        for(int i = 0; currentTime < duration; i++){
            currentTime = (double)i/frequency;
            Point point = new Point(currentTime, getValue(currentTime));
            points.add(point);
        }
    }

    public List<Point> getPoints() {
        if(points == null){
           computePoints();
        }

        return points;
    }

    public abstract Double getValue(Double t);

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Czest.: ").append(frequency).append(" ");
        stringBuilder.append("Ampl.: ").append(amplitude).append(" ");
        stringBuilder.append("Czas start.: ").append(startTime).append(" ");
        stringBuilder.append("Czas: ").append(duration);

        return stringBuilder.toString();
    }
}
