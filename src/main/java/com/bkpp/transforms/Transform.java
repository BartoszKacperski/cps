package com.bkpp.transforms;

import com.bkpp.signals.Signal;

public interface Transform {
    TransformResult transform(Signal signal);
}
