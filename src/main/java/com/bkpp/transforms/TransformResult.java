package com.bkpp.transforms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.complex.Complex;

import java.io.Serializable;
import java.util.List;

import com.bkpp.signals.Signal;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class TransformResult implements Serializable {
    private long executionTimeInMillis;
    private List<Complex> result;
    private Signal signal;
}
