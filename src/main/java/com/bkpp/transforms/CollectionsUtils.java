package com.bkpp.transforms;

import com.bkpp.signals.Point;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionsUtils {

    public static double[] fromList(List<Double> list){
        double [] array = new double[list.size()];

        for(int i = 0; i < list.size(); i++){
            array[i] = list.get(i);
        }

        return array;
    }

    public static double[] fromListToFourier(List<Double> list){
        double [] array = new double[list.size() * 2];

        for(int i = 0; i < list.size(); i++){
            array[i] = list.get(i);
        }

        return array;
    }

    public static List<Double> toList(double [] array){
        List<Double> list = new ArrayList<>();

        for(double v : array){
            list.add(v);
        }

        return list;
    }

    public static List<Double> map(List<Point> list){
        return list.stream().map(Point::getY).collect(Collectors.toList());
    }

    public static Complex[] mapToComplex(List<Double> list){
        Complex [] complexes = new Complex[list.size()];

        for(int i = 0; i < complexes.length; i++){
            complexes[i] = new Complex(list.get(i), 0);
        }

        return complexes;
    }
}
