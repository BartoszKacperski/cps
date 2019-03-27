package com.bkpp.signals;

import com.bkpp.Point;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data

public class ResultSignal extends Signal {
    public enum Operation {
        multiplication("mnozenie"), addition("dodawanie"), division("dzielenie"), subtraction("odejmowanie");

        private String name;

        Operation(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

    private Operation operation;

    public ResultSignal(List<Point> pointList, Operation operation) {
        this.setPoints(pointList);
        this.operation = operation;
    }

    @Override
    public Double getValue(Double t) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public String toString(){
        return "Sygnal wynikowy (amplituda: " + super.getAmplitude() + ") " + operation.getName();
    }
}
