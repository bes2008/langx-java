package com.jn.langx.util.ranges;

public class IntRange extends Range<Integer> {
    public IntRange() {
        this(0, 0);
    }

    public IntRange(Integer start, Integer endInclusive) {
        super(start, endInclusive);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Integer value) {
        if (value == null) {
            return false;
        }
        if (getStart() != null) {
            if (value < getStart()) {
                return false;
            }
        }
        if (getEndInclusive() != null) {
            if (value > getEndInclusive()) {
                return false;
            }
        }
        return true;
    }
}
