package com.bkpp.signals;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.function.DoubleBinaryOperator;

@AllArgsConstructor
@Data

public class ResultSignal extends Signal {
    public enum Operation {
        multiplication("mnozenie"), addition("dodawanie"), division("dzielenie"), subtraction("odejmowanie");

        private String name;

        Operation(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private Operation operation;
    private Signal firstSignal;
    private Signal secondSignal;

    public ResultSignal(Signal firstSignal, Signal secondSignal, Operation operation) {
        this.firstSignal = firstSignal;
        this.secondSignal = secondSignal;
        this.operation = operation;
    }

    @Override
    public Double getValue(Double t) {
        switch (operation) {
            case addition:
                return firstSignal.getValue(t) + secondSignal.getValue(t);
            case division:
                double value = secondSignal.getValue(t);
                if (Math.abs(value - 0.0) < 10E-15) {
                    return 0.0;
                }
                return firstSignal.getValue(t) / value;
            case subtraction:
                return firstSignal.getValue(t) - secondSignal.getValue(t);
            case multiplication:
                return firstSignal.getValue(t) * secondSignal.getValue(t);
            default:
                return 0.0;
        }
    }

    @Override
    public String toString() {
        return "Sygnal wynikowy (amplituda: " + super.getAmplitude() + ") " + operation.getName();
    }
}
