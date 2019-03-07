package com.bkpp.signals;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public abstract class ContinuousSignal extends Signal {
    protected Double term;
}
