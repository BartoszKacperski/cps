package com.bkpp.converters;

import com.bkpp.signals.Point;
import com.bkpp.signals.Signal;
import com.bkpp.signals.SignalUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Converter {
    private int bytes;
    private Signal signal;
    private List<Point> quantizedPoints;
    private List<Point> reconstructedPoints;

    public Converter(Signal signal, int bytes){
        this.signal = signal;
        this.bytes = bytes;
    }

    private List<Point> samplePoints(List<Point> signalPoints, int sampling) {
        return IntStream.range(0, signalPoints.size())
                .filter(n -> n % sampling == 0)
                .mapToObj(signalPoints::get)
                .collect(Collectors.toList());
    }

    public List<Point> quantization(int sampling){
        quantizedPoints = new ArrayList<>(samplePoints(signal.getPoints(), sampling));
        final double q = Math.pow(2.0, 1.0 - bytes);

        quantizedPoints.forEach(p -> {
            double y = Math.floor((p.getY()/q) + 0.5) * q;
            p.setY(y);
        });

        return quantizedPoints;
    }

    public List<Point> reconstruction(){
        if(quantizedPoints == null){
            throw new IllegalStateException();
        }

        reconstructedPoints = new ArrayList<>();
        double Ts = quantizedPoints.get(1).getX() - quantizedPoints.get(0).getY();

        for(Point point : signal.getPoints()){
            double x = point.getX();
            double value = reconstructedValue(x, Ts);
            reconstructedPoints.add(new Point(x, value));
        }


        return reconstructedPoints;
    }

    private double reconstructedValue(double t, double Ts){
        double sum = 0.0;
        for(int i = 0; i < quantizedPoints.size(); i++){
            sum += quantizedPoints.get(i).getY() * sinc(t/Ts - i);
        }

        return sum;
    }

    private double sinc(double t){
        if(Math.abs(t - 0.0) < 1E-10){
            return 1.0;
        }

        return Math.sin(Math.PI * t)/(Math.PI * t);
    }

    private double squaredSum(){
        double sum = 0.0;

        for(int i = 0; i < reconstructedPoints.size() && i < signal.getPoints().size(); i++){
            Point reconstructedPoint = reconstructedPoints.get(i);
            Point signalPoint = signal.getPoints().get(i);

            sum += Math.pow(reconstructedPoint.getY() - signalPoint.getY(), 2.0);
        }

        return sum;
    }

    public double meanSquaredError(){
        return squaredSum()/reconstructedPoints.size();
    }

    public double signalNoiseRatio(){
        double signalSum = 0.0;

        for(Point point : signal.getPoints()){
            signalSum += Math.pow(point.getY(), 2.0);
        }

        return 10.0 * Math.log10(signalSum/squaredSum());
    }

    public double peakSignalNoiseRatio(){
        double max = signal.getPoints().stream().max(Comparator.comparingDouble(Point::getY)).get().getY();

        return 10.0 * Math.log10(max/meanSquaredError());
    }

    public double maximalDifference(){
        double maxDifference = 0.0;

        for(int i = 0; i < reconstructedPoints.size() && i < signal.getPoints().size(); i++){
            Point reconstructedPoint = reconstructedPoints.get(i);
            Point signalPoint = signal.getPoints().get(i);

            if(Math.abs(reconstructedPoint.getX() - signalPoint.getX()) < 1E-10){
                double difference =  Math.abs(reconstructedPoint.getY() - signalPoint.getY());
                if(maxDifference < difference){
                    maxDifference = difference;
                }
            }
        }

        return maxDifference;
    }

}
