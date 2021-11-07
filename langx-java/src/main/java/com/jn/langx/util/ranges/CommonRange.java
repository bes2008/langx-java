package com.jn.langx.util.ranges;

public class CommonRange<T extends Comparable<T>> extends Range<T> {
    public CommonRange(T start, T end) {
        super(start, end);
    }

    @Override
    public boolean contains(T value) {
        if (isEmpty()) {
            return value == null;
        }
        T start = getStart();
        if (start != null) {
            int delta = start.compareTo(value);
            if (delta > 0) {
                return false;
            }
        }

        T end = getEndInclusive();
        if (end != null) {
            int delta = end.compareTo(value);
            if (delta < 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return getStart() == null && getEndInclusive() == null;
    }
}
