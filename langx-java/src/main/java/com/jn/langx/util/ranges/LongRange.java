package com.jn.langx.util.ranges;

public class LongRange extends Range<Long> {
    public LongRange() {
        this(0L, 0L);
    }

    public LongRange(Long start, Long endInclusive) {
        super(start, endInclusive);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Long value) {
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
