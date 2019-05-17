package com.bkpp.transforms;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastFourierTransform implements Transform {

    @Override
    public TransformResult transform(List<Double> list) {
        long startTime = System.nanoTime();
        Complex[] result = new FastFourierTransformer(DftNormalization.STANDARD).transform(CollectionsUtils.fromList(list), TransformType.FORWARD);
        long duration = System.nanoTime() - startTime;

        List<Complex> complexes = new ArrayList<>(Arrays.asList(result));

        return new TransformResult(duration, complexes);
    }


    private Complex[] fft(Complex[] x) {
        int n = x.length;

        // base case
        if (n == 1) return new Complex[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }

        // fft of even terms
        Complex[] even = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < n/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].add(wk.multiply(r[k]));
            y[k + n/2] = q[k].add(wk.multiply(r[k]));
        }
        return y;
    }

    private boolean isPowerOfTwo(int number) {

        return number > 0 && ((number & (number - 1)) == 0);

    }

    @Override
    public String toString(){
        return "FFT";
    }
}
