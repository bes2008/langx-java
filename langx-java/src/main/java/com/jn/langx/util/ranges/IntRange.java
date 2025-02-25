package com.jn.langx.util.ranges;

public class IntRange extends NumberRange<Integer> {
    public IntRange() {
        this(0, 0);
    }

    public IntRange(Integer start){
        this(start, Integer.MAX_VALUE);
    }

    public IntRange(Integer start, Integer endInclusive) {
        super(start, endInclusive);
    }

    public IntRange(Integer start, Integer end, boolean startInclusive, boolean endInclusive){
        super(start, end, startInclusive, endInclusive);
    }
}
