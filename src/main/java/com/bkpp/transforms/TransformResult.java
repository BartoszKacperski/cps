package com.bkpp.transforms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.complex.Complex;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TransformResult {
    private long executionTimeInMillis;
    private List<Complex> result;
}
