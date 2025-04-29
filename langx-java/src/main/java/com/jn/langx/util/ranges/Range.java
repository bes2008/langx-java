package com.jn.langx.util.ranges;

import com.jn.langx.util.EmptyEvalutible;

public abstract class Range<T> implements EmptyEvalutible {
    private T start;
    private T end;

    private boolean startInclusive;
    private boolean endInclusive;

    protected Range(T start, T endInclusive){
        this(start, endInclusive, true,true);
    }

    protected Range(T start, T end, boolean startInclusive, boolean endInclusive){
        setStart(start);
        setEnd(end);
        setStartInclusive(startInclusive);
        setEndInclusive(endInclusive);
    }



    public T getStart() {
        return start;
    }

    public void setStart(T start) {
        this.start = start;
    }

    public void setEnd(T end) {
        this.end = end;
    }

    public T getEnd() {
        return end;
    }

    @Deprecated
    public void setEndInclusive(boolean endInclusive) {
        this.endInclusive = endInclusive;
    }

    public void setStartInclusive(boolean startInclusive) {
        this.startInclusive = startInclusive;
    }

    public boolean isEndInclusive() {
        return endInclusive;
    }

    public boolean isStartInclusive() {
        return startInclusive;
    }

    public void setEndInclusive(T end) {
        setEnd(end);
        setEndInclusive(true);
    }

    public abstract boolean contains(T value);

    @Override
    public boolean isNull() {
        return false;
    }
}
