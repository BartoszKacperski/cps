package com.bkpp.signals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SignalUtils {

    private SignalUtils() {

    }

    public static Signal addition(Signal firstSignal, Signal secondSignal) {
        ResultSignal sumSignal = new ResultSignal(firstSignal, secondSignal, ResultSignal.Operation.addition);
        sumSignal.setAmplitude(firstSignal.getAmplitude() + secondSignal.getAmplitude());
        sumSignal.setDuration(firstSignal.getDuration());
        sumSignal.setFrequency(firstSignal.getFrequency());
        sumSignal.setStartTime(firstSignal.getStartTime());

        if (firstSignal instanceof TermSignal && secondSignal instanceof TermSignal) {
            double firstTerm = ((TermSignal) firstSignal).getTerm();
            double secondTerm = ((TermSignal) secondSignal).getTerm();

            sumSignal.setTerm(firstTerm + secondTerm);
        }

        return sumSignal;
    }

    public static Signal subtraction(Signal firstSignal, Signal secondSignal) {
        Signal differenceSignal = new ResultSignal(firstSignal, secondSignal, ResultSignal.Operation.subtraction);
        differenceSignal.setAmplitude(firstSignal.getAmplitude() - secondSignal.getAmplitude());
        differenceSignal.setDuration(firstSignal.getDuration());
        differenceSignal.setFrequency(firstSignal.getFrequency());
        differenceSignal.setStartTime(firstSignal.getStartTime());

        return differenceSignal;
    }

    public static Signal multiplication(Signal firstSignal, Signal secondSignal) {
        Signal productSignal = new ResultSignal(firstSignal, secondSignal, ResultSignal.Operation.multiplication);
        productSignal.setAmplitude(firstSignal.getAmplitude() - secondSignal.getAmplitude());
        productSignal.setDuration(firstSignal.getDuration());
        productSignal.setFrequency(firstSignal.getFrequency());
        productSignal.setStartTime(firstSignal.getStartTime());

        return productSignal;
    }

    public static Signal division(Signal firstSignal, Signal secondSignal) {

        Signal quotientSignal = new ResultSignal(firstSignal, secondSignal, ResultSignal.Operation.division);
        quotientSignal.setAmplitude(firstSignal.getAmplitude() - secondSignal.getAmplitude());
        quotientSignal.setDuration(firstSignal.getDuration());
        quotientSignal.setFrequency(firstSignal.getFrequency());
        quotientSignal.setStartTime(firstSignal.getStartTime());

        return quotientSignal;
    }

    private static List<Point> adjustPointsIfTermSignal(Signal signal) {
        if (signal instanceof TermSignal) {
            TermSignal termSignal = (TermSignal) signal;
            final double lastXInTerm = ((int) (termSignal.getDuration() / termSignal.getTerm()) * termSignal.getTerm());

            return signal.getPoints().stream().filter(point -> point.getX() <= lastXInTerm).collect(Collectors.toList());
        } else {
            return signal.getPoints();
        }
    }

    public static double averageValue(Signal signal) {
        return averageValue(adjustPointsIfTermSignal(signal));
    }

    public static double averageValue(List<Point> points) {
        int firstValue = 0;
        int lastValue = points.size();

        double fraction = 1.0 / (lastValue - firstValue + 1.0);

        double sum = 0.0;

        for (Point point : points) {
            sum += point.getY();
        }

        return fraction * sum;
    }

    public static double absoluteAverageValue(Signal signal) {
        return absoluteAverageValue(adjustPointsIfTermSignal(signal));
    }

    public static double absoluteAverageValue(List<Point> points) {
        int firstValue = 0;
        int lastValue = points.size();

        double fraction = 1.0 / (lastValue - firstValue + 1.0);

        double sum = 0.0;

        for (Point point : points) {
            sum += Math.abs(point.getY());
        }

        return fraction * sum;
    }

    public static double power(Signal signal) {
        return power(adjustPointsIfTermSignal(signal));
    }

    public static double power(List<Point> points) {
        int firstValue = 0;
        int lastValue = points.size();

        double fraction = 1.0 / (lastValue - firstValue + 1.0);

        double sum = 0.0;

        for (Point point : points) {
            sum += Math.pow(point.getY(), 2.0);
        }

        return fraction * sum;
    }

    public static double variance(Signal signal) {
        return variance(adjustPointsIfTermSignal(signal));
    }

    public static double variance(List<Point> points) {
        int firstValue = 0;
        int lastValue = points.size();

        double fraction = 1.0 / (lastValue - firstValue + 1.0);
        double avg = averageValue(points);
        double sum = 0.0;

        for (Point point : points) {
            sum += Math.pow(point.getY() - avg, 2.0);
        }

        return fraction * sum;
    }

    public static double effectiveValue(Signal signal) {
        return effectiveValue(signal.getPoints());
    }

    public static double effectiveValue(List<Point> points) {
        return Math.sqrt(power(points));
    }

    public static Signal convolution(Signal firstSignal, Signal secondSignal) {
        List<Point> firstPoints = firstSignal.getPoints();
        List<Point> secondPoints = secondSignal.getPoints();
        List<Point> resultPoints = new ArrayList<>();

        for (int i = 0;i < firstPoints.size() + secondPoints.size() - 1;i++) {
            double sum = 0.0;

            for (int j = 0;j < firstPoints.size();j++) {
                if ((i - j) >= 0 && (i - j) < secondPoints.size()) {
                    sum += firstPoints.get(j).getY() * secondPoints.get(i - j).getY();
                }
            }

            resultPoints.add(new Point((double) i, sum));
        }

        return new ResultSignal(resultPoints, ResultSignal.Operation.convulsion);
    }

    public static Signal correlation(Signal firstSignal, Signal secondSignal) {
        List<Point> firstPoints = firstSignal.getPoints();
        List<Point> secondPoints = secondSignal.getPoints();
        List<Point> resultPoints = new ArrayList<>();
        int length = firstPoints.size() + secondPoints.size() - 1;


        for(int n = 1 - secondPoints.size(); n < firstPoints.size(); n++){
            double sum = 0.0;

            for(int k = 0; k < firstPoints.size(); k++){
                if(k -n >= secondPoints.size() || k - n < 0){
                    continue;
                }

                sum += firstPoints.get(k).getY() * secondPoints.get(k - n).getY();
            }

            resultPoints.add(new Point((double) n, sum));
        }

        return new ResultSignal(resultPoints, ResultSignal.Operation.correlation);
    }
}
