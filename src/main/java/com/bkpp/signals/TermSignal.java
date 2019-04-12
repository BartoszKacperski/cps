package com.bkpp.signals;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

public abstract class TermSignal extends Signal {
    protected Double term;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Okres: ").append(term).append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }
}
