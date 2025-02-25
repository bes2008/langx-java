package com.jn.langx.validator;

import com.jn.langx.util.converter.DoubleConverter;
import com.jn.langx.util.ranges.DoubleRange;

public class DoubleRangeRule extends RangRule<Double> {
    public DoubleRangeRule(DoubleRange range, String errorMessage) {
        super(range, Double.class, DoubleConverter.INSTANCE, errorMessage);
    }
}
