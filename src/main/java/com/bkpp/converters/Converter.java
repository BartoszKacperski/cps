package com.bkpp.converters;

import com.bkpp.signals.Point;
import com.bkpp.signals.Signal;
import lombok.Data;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data

public class Converter {
    private int bytes;
    private double frequency;
    private Signal signal;
    private List<Point> sampledPoints;
    private List<Point> quantizedPoints;
    private List<Point> reconstructedPoints;

    public Converter(Signal signal, double frequency, int bytes){
        this.signal = signal;
        this.frequency = frequency;
        this.bytes = bytes;
    }

    public List<Point> samplePoints(){
        Signal signalForSample = SerializationUtils.clone(signal);

        signalForSample.setFrequency(frequency);
        signalForSample.computePoints();
        sampledPoints = signalForSample.getPoints();

        return sampledPoints;
    }

    public List<Point> quantization(){
        if(sampledPoints == null){
            throw new IllegalStateException("Sample first");
        }
        quantizedPoints = new ArrayList<>();

        final double q = Math.pow(2.0, -bytes + 1);

        for(Point point : sampledPoints){
            double x = point.getX();
            double y = point.getY();

            y = Math.floor((y/q) + 0.5) * q;

            quantizedPoints.add(new Point(x, y));
        }


        return quantizedPoints;
    }

    public List<Point> reconstruction(int neighbour){
        if(sampledPoints == null){
            throw new IllegalStateException("Sample first");
        }

        reconstructedPoints = new ArrayList<>();
        double Ts = 1.0/frequency;

        for(Point point : signal.getPoints()){
            double x = point.getX();
            double value = reconstructedValue(x, Ts, neighbour);
            reconstructedPoints.add(new Point(x, value));
        }

        return reconstructedPoints;
    }

    private double reconstructedValue(double t, double Ts, int neighbour){
        double sum = 0.0;

        Point closestPoint = sampledPoints
                .stream()
                .min((p1, p2) -> {
                    double distance1 = Math.abs(p1.getX() - t);
                    double distance2 = Math.abs(p2.getX() - t);
                    return Double.compare(distance1, distance2);
                }).get();

        int indexOfClosest = sampledPoints.indexOf(closestPoint);
        int startIndex = indexOfClosest - neighbour/2 < 0 ? 0 : indexOfClosest - neighbour/2;
        int endIndex= indexOfClosest + neighbour/2 > sampledPoints.size() ? sampledPoints.size() : indexOfClosest + neighbour/2;

        for(int i = startIndex; i < endIndex; i++){
            sum += sampledPoints.get(i).getY() * sinc(t/Ts - i);
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
        if(reconstructedPoints == null){
            throw new IllegalStateException("Reconstruct first");
        }
        return squaredSum()/reconstructedPoints.size();
    }

    public double signalNoiseRatio(){
        if(reconstructedPoints == null){
            throw new IllegalStateException("Reconstruct first");
        }
        double signalSum = 0.0;

        for(Point point : signal.getPoints()){
            signalSum += Math.pow(point.getY(), 2.0);
        }

        return 10.0 * Math.log10(signalSum/squaredSum());
    }

    public double peakSignalNoiseRatio(){
        if(reconstructedPoints == null){
            throw new IllegalStateException("Reconstruct first");
        }
        double max = signal.getPoints().stream().max(Comparator.comparingDouble(Point::getY)).get().getY();

        return 10.0 * Math.log10(max/meanSquaredError());
    }

    public double maximalDifference(){
        if(reconstructedPoints == null){
            throw new IllegalStateException("Reconstruct first");
        }
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
