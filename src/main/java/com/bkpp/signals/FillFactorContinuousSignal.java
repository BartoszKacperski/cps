package com.bkpp.signals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public abstract class FillFactorContinuousSignal extends ContinuousSignal {
    protected Integer k = 1;
    protected Double fillFactor;
}
