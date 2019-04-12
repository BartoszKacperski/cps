package com.bkpp.signals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Point implements Serializable {
    private Double x;
    private Double y;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Math.abs(getX() - point.getX()) < 10E-15 &&
                Math.abs(getY() - point.getY()) < 10E-15;
    }
}
