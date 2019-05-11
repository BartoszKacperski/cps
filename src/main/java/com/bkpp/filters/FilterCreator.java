package com.bkpp.filters;

import com.bkpp.signals.*;
import com.sun.javafx.scene.paint.MaterialHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data

public class FilterCreator {
    private static final double EPS = 10E-15;
    private double M;
    private double f0;
    private Signal signal;

    public Signal createFilter(Window window, Filter filter) {
        double fp = Math.ceil(1.0/(signal.getPoints().get(1).getX() - signal.getPoints().get(0).getX()));
        f0 = filter.frequency(fp, f0);
        double k = fp/f0;

        List<Point> points = new ArrayList<>();

        for (int i = 0;i < M;i++) {
            double y = getH(i, k) * window.getValue(i, M) * filter.filterValue(i);
            points.add(new Point((double) i, y));
        }

        return new FilterSignal(points);
    }

    private double getH(double i, double k) {
        if (Math.abs(i - ((M - 1) / 2)) < EPS) {
            return 2.0 / k;
        }

        double top = Math.sin(2.0 * Math.PI * (i - (M - 1)/ 2) / k);
        double bottom = Math.PI * (i - (M - 1) / 2);

        return  top/bottom;
    }

}
