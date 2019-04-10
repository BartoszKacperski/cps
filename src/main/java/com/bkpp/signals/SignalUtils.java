package com.bkpp.signals;

import com.bkpp.signals.Point;
import com.bkpp.signals.ResultSignal;
import com.bkpp.signals.Signal;
import com.bkpp.signals.PeriodSignal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SignalUtils {

    private SignalUtils(){

    }

    public static Signal addition(Signal firstSignal, Signal secondSignal){
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

        Signal sumSignal = new ResultSignal(sum, ResultSignal.Operation.addition);
        sumSignal.setAmplitude(firstSignal.getAmplitude() + secondSignal.getAmplitude());
        sumSignal.setDuration(firstSignal.getDuration());
        sumSignal.setFrequency(firstSignal.getFrequency());
        sumSignal.setStartTime(firstSignal.getStartTime());

        return sumSignal;
    }

    public static Signal subtraction(Signal firstSignal, Signal secondSignal){
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

        Signal differenceSignal = new ResultSignal(difference, ResultSignal.Operation.subtraction);
        differenceSignal.setAmplitude(firstSignal.getAmplitude() - secondSignal.getAmplitude());
        differenceSignal.setDuration(firstSignal.getDuration());
        differenceSignal.setFrequency(firstSignal.getFrequency());
        differenceSignal.setStartTime(firstSignal.getStartTime());

        return differenceSignal;
    }

    public static Signal multiplication(Signal firstSignal, Signal secondSignal){
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

        Signal productSignal = new ResultSignal(product, ResultSignal.Operation.multiplication);
        productSignal.setAmplitude(firstSignal.getAmplitude() - secondSignal.getAmplitude());
        productSignal.setDuration(firstSignal.getDuration());
        productSignal.setFrequency(firstSignal.getFrequency());
        productSignal.setStartTime(firstSignal.getStartTime());

        return productSignal;
    }

    public static Signal division(Signal firstSignal, Signal secondSignal){
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

        Signal quotientSignal = new ResultSignal(quotient, ResultSignal.Operation.division);
        quotientSignal.setAmplitude(firstSignal.getAmplitude() - secondSignal.getAmplitude());
        quotientSignal.setDuration(firstSignal.getDuration());
        quotientSignal.setFrequency(firstSignal.getFrequency());
        quotientSignal.setStartTime(firstSignal.getStartTime());

        return quotientSignal;
    }

    private static List<Point> adjustPointsIfTermSignal(Signal signal){
        if(signal instanceof PeriodSignal){
            PeriodSignal periodSignal = (PeriodSignal)signal;
            final double lastXInTerm = ((int)(periodSignal.getDuration()/ periodSignal.getPeriod()) * periodSignal.getPeriod());

            return signal.getPoints().stream().filter(point -> point.getX() <= lastXInTerm).collect(Collectors.toList());
        } else {
            return signal.getPoints();
        }
    }

    public static double averageValue(Signal signal){
        return averageValue(adjustPointsIfTermSignal(signal));
    }

    public static double averageValue(List<Point> points){
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
        return absoluteAverageValue(adjustPointsIfTermSignal(signal));
    }

    public static double absoluteAverageValue(List<Point> points){
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
        return power(adjustPointsIfTermSignal(signal));
    }

    public static double power(List<Point> points){
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
        return variance(adjustPointsIfTermSignal(signal));
    }

    public static double variance(List<Point> points){
        int firstValue = 0;
        int lastValue = points.size();

        double fraction = 1.0/(lastValue - firstValue + 1.0);
        double avg = averageValue(points);
        double sum = 0.0;

        for(Point point : points){
            sum += Math.pow(point.getY() - avg, 2.0);
        }

        return fraction * sum;
    }

    public static double effectiveValue(Signal signal){
        return effectiveValue(signal.getPoints());
    }

    public static double effectiveValue(List<Point> points){
        return Math.sqrt(power(points));
    }
}
