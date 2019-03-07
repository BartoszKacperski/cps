package com.bkpp.signals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor

public class UnitaryJump extends ExtendedNoise {

    @Override
    public Double getValue(Double t) {
        Double jumpTime = parameter;

        if(t > jumpTime){
            return amplitude;
        } else if (Math.abs(t - parameter) < 1E-10){
            return 0.5;
        } else {
            return 0.0;
        }
    }

    @Override
    public String toString(){
        return "Skok jednostkowy";
    }
}
