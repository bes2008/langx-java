package com.jn.langx.validation.rule;

import com.jn.langx.util.converter.DoubleConverter;
import com.jn.langx.util.ranges.DoubleRange;

public class DoubleRangeRule extends RangRule<Double> {
    public DoubleRangeRule(String errorMessage, DoubleRange range) {
        super(errorMessage, range, Double.class, DoubleConverter.INSTANCE);
    }
}
