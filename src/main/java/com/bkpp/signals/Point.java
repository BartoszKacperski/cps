package com.bkpp.signals;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Point implements Serializable {
    private Double x;
    private Double y;
}
