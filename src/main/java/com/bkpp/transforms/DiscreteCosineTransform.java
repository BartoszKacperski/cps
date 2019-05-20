package com.bkpp.transforms;

import org.apache.commons.math3.complex.Complex;
import com.bkpp.signals.Signal;

import java.util.ArrayList;
import java.util.List;

public class DiscreteCosineTransform implements Transform {

    @Override
    public TransformResult transform(Signal signal) {
        List<Double> y = CollectionsUtils.map(signal.getPoints());

        long startTime = System.nanoTime();
        List<Complex> complexes = dct(y);
        long duration = System.nanoTime() - startTime;

        return new TransformResult(duration, complexes, signal);
    }

    private List<Complex> dct(List<Double> y){
        List<Complex> complexes = new ArrayList<>();

        for(int i = 0; i < y.size(); i++){
            double sum = 0.0;
            for(int j = 0; j < y.size(); j++){
                double value = y.get(j);
                double top = Math.PI * (2 * j + 1) * i;
                double bottom = 2 * y.size();

                sum += value * Math.cos(top/bottom);
            }
            double Cm = Math.sqrt(2.0/(double)y.size());
            complexes.add(new Complex(Cm * sum, 0));
        }

        return complexes;
    }

    @Override
    public String toString(){
        return "DCT";
    }
}
