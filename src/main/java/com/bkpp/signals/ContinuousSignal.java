package com.bkpp.signals;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public abstract class ContinuousSignal extends Signal {
    protected Double term;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Okres: ").append(term).append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }
}
