package com.bkpp.signals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor


public abstract class FillFactorTermSignal extends TermSignal {
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
