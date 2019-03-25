package com.bkpp.signals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UnitaryJump extends Signal {
    private Double jumpTime;

    @Override
    public Double getValue(Double t) {
        if (t > jumpTime) {
            return amplitude;
        } else if (Math.abs(t - jumpTime) < 1E-10) {
            return 0.5;
        } else {
            return 0.0;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Skok jednostkowy").append(" ");
        stringBuilder.append(super.toString());

        return stringBuilder.toString();
    }
}
