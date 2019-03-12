package com.bkpp.signals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public abstract class DiscreteSignal extends Signal{
    protected Double parameter;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Parametr: ").append(parameter).append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }
}
