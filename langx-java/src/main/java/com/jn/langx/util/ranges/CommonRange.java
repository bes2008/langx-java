package com.jn.langx.util.ranges;

public class CommonRange<T extends Comparable<T>> extends Range<T> {
    public CommonRange(T start, T end, boolean startInclusive, boolean endInclusive) {
        super(start, end, startInclusive, endInclusive);
    }

    public CommonRange(T start, T endInclusive) {
        super(start, endInclusive, true, true);
    }


    @Override
    public boolean contains(T value) {
        if (isEmpty()) {
            return value == null;
        }
        T start = getStart();
        if (start != null) {
            int delta = start.compareTo(value);
            if(isStartInclusive()){
                if (delta > 0) {
                    return false;
                }
            }else{
                if (delta >= 0) {
                    return false;
                }
            }

        }

        T end = getEnd();
        if (end != null) {
            int delta = end.compareTo(value);
            if(isEndInclusive()){
                return delta >= 0;
            }else{
                return delta > 0;
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return getStart() == null && getEnd() == null;
    }
}
