package com.bkpp.transforms;

import java.util.List;

public interface Transform {
    TransformResult transform(List<Double> y);
}
