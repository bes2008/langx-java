package com.jn.langx.util.ranges;

public class EmptyRange extends Range {
    public EmptyRange() {
        this(null, null);
    }

    public EmptyRange(Object start, Object endInclusive) {
        super(start, endInclusive);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean contains(Object value) {
        return false;
    }
}
