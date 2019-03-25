package com.bkpp.signals;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

public abstract class PeriodSignal extends Signal {
    protected Double period;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Okres: ").append(period).append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }
}
