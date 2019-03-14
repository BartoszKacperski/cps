package com.bkpp;

import com.bkpp.signals.Signal;
import com.bkpp.signals.TermSignal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SignalUtils {

    private SignalUtils(){

    }

    public static List<Point> addition(Signal firstSignal, Signal secondSignal){
        List<Point> firstPoints = firstSignal.getPoints();
        List<Point> secondPoints = secondSignal.getPoints();
        List<Point> sum = new ArrayList<>();

        for(int i = 0; i < firstPoints.size() && i < secondPoints.size(); i++){
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
        List<Point> difference = new ArrayList<>();

        for(int i = 0; i < firstPoints.size() && i < secondPoints.size(); i++){
            Point firstPoint = firstPoints.get(i);
            Point secondPoint = secondPoints.get(i);

            if(Math.abs(firstPoint.getX() - secondPoint.getX()) < 1E-10){
                difference.add(new Point(firstPoint.getX(), firstPoint.getY() - secondPoint.getY()));
            }
        }

        return difference;
    }

    public static List<Point> multiplication(Signal firstSignal, Signal secondSignal){
        List<Point> firstPoints = firstSignal.getPoints();
        List<Point> secondPoints = secondSignal.getPoints();
        List<Point> product = new ArrayList<>();

        for(int i = 0; i < firstPoints.size() && i < secondPoints.size(); i++){
            Point firstPoint = firstPoints.get(i);
            Point secondPoint = secondPoints.get(i);

            if(Math.abs(firstPoint.getX() - secondPoint.getX()) < 1E-10){
                product.add(new Point(firstPoint.getX(), firstPoint.getY() * secondPoint.getY()));
            }
        }

        return product;
    }

    public static List<Point> division(Signal firstSignal, Signal secondSignal){
        List<Point> firstPoints = firstSignal.getPoints();
        List<Point> secondPoints = secondSignal.getPoints();
        List<Point> quotient = new ArrayList<>();

        for(int i = 0; i < firstPoints.size() && i < secondPoints.size(); i++){
            Point firstPoint = firstPoints.get(i);
            Point secondPoint = secondPoints.get(i);

            if(Math.abs(firstPoint.getX() - secondPoint.getX()) < 1E-10 && secondPoint.getY() != 0){
                quotient.add(new Point(firstPoint.getX(), firstPoint.getY() / secondPoint.getY()));
            }
        }

        return quotient;
    }

    private static List<Point> adjustPointsIfTermSignal(Signal signal){
        if(signal instanceof TermSignal){
            TermSignal termSignal = (TermSignal)signal;
            final double lastXInTerm = ((int)(termSignal.getDuration()/termSignal.getTerm()) * termSignal.getTerm());

            return signal.getPoints().stream().filter(point -> point.getX() <= lastXInTerm).collect(Collectors.toList());
        } else {
            return signal.getPoints();
        }
    }

    public static double averageValue(Signal signal){
        List<Point> points = adjustPointsIfTermSignal(signal);
        int firstValue = 0;
        int lastValue = points.size();

        double fraction = 1.0/(lastValue - firstValue + 1.0);

        double sum = 0.0;

        for(Point point : points){
            sum += point.getY();
        }

        return fraction * sum;
    }

    public static double absoluteAverageValue(Signal signal){
        List<Point> points = adjustPointsIfTermSignal(signal);
        int firstValue = 0;
        int lastValue = points.size();

        double fraction = 1.0/(lastValue - firstValue + 1.0);

        double sum = 0.0;

        for(Point point : points){
            sum += Math.abs(point.getY());
        }

        return fraction * sum;
    }

    public static double power(Signal signal){
        List<Point> points = adjustPointsIfTermSignal(signal);
        int firstValue = 0;
        int lastValue = points.size();

        double fraction = 1.0/(lastValue - firstValue + 1.0);

        double sum = 0.0;

        for(Point point : points){
            sum += Math.pow(point.getY(), 2.0);
        }

        return fraction * sum;
    }

    public static double variance(Signal signal){
        List<Point> points = adjustPointsIfTermSignal(signal);
        int firstValue = 0;
        int lastValue = points.size();

        double fraction = 1.0/(lastValue - firstValue + 1.0);
        double avg = averageValue(signal);
        double sum = 0.0;

        for(Point point : points){
            sum += Math.pow(point.getY() - avg, 2.0);
        }

        return fraction * sum;
    }

    public static double effectiveValue(Signal signal){
        return Math.sqrt(power(signal));
    }

}
