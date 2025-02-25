package com.jn.langx.validation;

import com.jn.langx.util.converter.DoubleConverter;
import com.jn.langx.util.ranges.DoubleRange;

public class DoubleRangeRule extends RangRule<Double> {
    public DoubleRangeRule(DoubleRange range, String errorMessage) {
        super(range, Double.class, DoubleConverter.INSTANCE, errorMessage);
    }
}
