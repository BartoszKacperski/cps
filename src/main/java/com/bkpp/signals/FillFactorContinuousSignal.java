package com.bkpp.signals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public abstract class FillFactorContinuousSignal extends ContinuousSignal {
    protected Integer k;
    protected Double fillFactor;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Wsp. wyp.: ").append(fillFactor).append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }
}
