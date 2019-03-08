package com.bkpp;

import com.bkpp.signals.Signal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignalOperationUtils {

    private SignalOperationUtils(){

    }

    public static List<Point> addition(Signal firstSignal, Signal secondSignal){
        List<Point> firstPoints = firstSignal.getPoints();
        List<Point> secondPoints = secondSignal.getPoints();

        if(firstPoints.size() != secondPoints.size()){
            //TODO
            return Collections.emptyList();
        }

        List<Point> sum = new ArrayList<>();

        for(int i = 0; i < firstPoints.size(); i++){
            Point firstPoint = firstPoints.get(i);
            Point secondPoint = secondPoints.get(i);

            if(Math.abs(firstPoint.getX() - secondPoint.getX()) < 1E-10){
                sum.add(new Point(firstPoint.getX(), firstPoint.getY() + secondPoint.getY()));
            }
        }

        return sum;
    }

    public static List<Point> subtraction(Signal firstSignal, Signal secondSignal){
        List<Point> firstPoints = firstSignal.getPoints();
        List<Point> secondPoints = secondSignal.getPoints();

        if(firstPoints.size() != secondPoints.size()){
            //TODO
            return Collections.emptyList();
        }

        List<Point> difference = new ArrayList<>();

        for(int i = 0; i < firstPoints.size(); i++){
            Point firstPoint = firstPoints.get(i);
            Point secondPoint = secondPoints.get(i);

            if(Math.abs(firstPoint.getX() - secondPoint.getX()) < 1E-10){
                difference.add(new Point(firstPoint.getX(), firstPoint.getY() - secondPoint.getY()));
            }
        }

        return difference;
    }

}
