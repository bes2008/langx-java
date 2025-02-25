package com.jn.langx.util.ranges;

public class IntRange extends CommonRange<Integer> {
    public IntRange() {
        this(0, 0);
    }

    public IntRange(Integer start){
        this(start, Integer.MAX_VALUE);
    }

    public IntRange(Integer start, Integer endInclusive) {
        super(start, endInclusive);
    }

    public String getRangeString(){
        return "["+this.getStart()+","+ (this.getEndInclusive()==Integer.MAX_VALUE?")": (this.getEndInclusive()+"]"));
    }
}
