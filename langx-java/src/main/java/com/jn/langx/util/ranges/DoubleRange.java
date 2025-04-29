package com.jn.langx.util.ranges;

public class DoubleRange extends NumberRange<Double> {
    public DoubleRange() {
        this(0d, 0d);
    }

    public DoubleRange(Double start){
        this(start, Double.MAX_VALUE);
    }

    public DoubleRange(Double start, Double endInclusive) {
        super(start, endInclusive);
    }

    public DoubleRange(Double start, Double end, boolean startInclusive, boolean endInclusive){
        super(start, end, startInclusive, endInclusive);
    }
}
