package com.bkpp.transforms;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

public class FastCosineTransform implements Transform {
    @Override
    public TransformResult transform(List<Double> y) {
        long startTime = System.nanoTime();
        List<Complex> complexes = fct(y);
        long duration = System.nanoTime() - startTime;

        return new TransformResult(duration, complexes);
    }

    private List<Complex> fct(List<Double> y){
        int size = y.size();
        int halfSize = y.size()/2;
        double [] tmp = new double[size];

        for(int i = 0; i < halfSize; i++){
            tmp[i] = y.get(i * 2);
            tmp[size - 1 - i] = y.get(i * 2 + 1);
        }

        if(size % 2 == 1){
            tmp[halfSize] = y.get(size - 1);
        }

        List<Complex> complexes = new FastFourierTransform().transform(CollectionsUtils.toList(tmp)).getResult();
        List<Complex> result = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            double temp = i * Math.PI / (size * 2.0);
            double realValue = complexes.get(i).getReal() * Math.cos(temp) + complexes.get(i).getImaginary() * Math.sin(temp);
            result.add(new Complex(realValue, 0.0));
        }

        return result;
    }

    @Override
    public String toString(){
        return "FCT";
    }
}
