package com.bkpp.histogram;

import com.bkpp.signals.Point;
import lombok.Getter;

import java.util.List;

public class Histogram {

    private List<Point> points;

    private int numberOfRanges;

    @Getter
    private int[] values;

    @Getter
    private double min;

    private double max;

    @Getter
    private double range;

    public Histogram(List<Point> points, int numberOfRanges) {
        this.points = points;
        this.numberOfRanges = numberOfRanges;
        this.values = new int[numberOfRanges];

        this.calculate();
    }

    private void setMinMax() {
        min = Double.MAX_VALUE;
        max = -Double.MAX_VALUE;

        for (Point point : points) {
            if (point.getY() > max) {
                max = point.getY();
            }
            if (point.getY() < min) {
                min = point.getY();
            }
        }
    }

    private void calculate() {
        this.setMinMax();

        range = (this.max - this.min) / this.numberOfRanges;

        for (Point point : points) {
            int index = (int) Math.ceil((point.getY() - min) / range) - 1;
            if (index < 0) {
                index = 0;
            }
            this.values[index]++;
        }
    }

}
