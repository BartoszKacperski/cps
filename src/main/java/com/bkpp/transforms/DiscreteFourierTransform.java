package com.bkpp.transforms;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

public class DiscreteFourierTransform implements Transform {
    @Override
    public TransformResult transform(List<Double> y) {
        double [] real = CollectionsUtils.fromList(y);
        double [] img = new double[y.size()];

        long startTime = System.nanoTime();
        List<Complex> complexes = dft(real, img);
        long duration = System.nanoTime() - startTime;

        return new TransformResult(duration, complexes);
    }

    private List<Complex> dft(double[] real, double[] img) {
        int n = real.length;
        List<Complex> complexes = new ArrayList<>();
        for (int k = 0; k < n; k++) {
            double realSum = 0;
            double imgSum = 0;
            for (int t = 0; t < n; t++) {
                double angle = 2 * Math.PI * t * k / n;
                realSum +=  real[t] * Math.cos(angle) + img[t] * Math.sin(angle);
                imgSum += -real[t] * Math.sin(angle) + img[t] * Math.cos(angle);
            }
            complexes.add(new Complex(realSum, imgSum));
        }

        return complexes;
    }

    @Override
    public String toString(){
        return "DFT";
    }
}
