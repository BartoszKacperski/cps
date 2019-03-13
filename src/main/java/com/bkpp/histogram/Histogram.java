package com.bkpp.histogram;

import com.bkpp.Point;
import lombok.Getter;

import java.util.List;

public class Histogram {

    private List<Point> points;

    private int numberOfRanges;

    @Getter
    private int[] values;

    private double min;

    private double max;

    public Histogram(List<Point> points, int numberOfRanges) {
        this.points = points;
        this.numberOfRanges = numberOfRanges;
        this.values = new int[numberOfRanges];

        this.calculate();
    }

    private void setMinMax() {
        min = 10e10;
        max = -10e10;

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

        double range = (this.max - this.min) / this.numberOfRanges;

        for (Point point : points) {
            int index = (int) Math.ceil((point.getY() - min) / range) - 1;
            if (index < 0) {
                index = 0;
            }
            this.values[index]++;
        }
    }

}