package com.jn.langx.util.ranges;

import com.jn.langx.util.Preconditions;

public class NumberRange<T extends Comparable<T>> extends CommonRange<T>{
    public NumberRange(T start, T endInclusive) {
        super(Preconditions.checkNotNull(start), Preconditions.checkNotNull(endInclusive));
    }
    public NumberRange(T start, T end, boolean startInclusive, boolean endInclusive) {
        super(Preconditions.checkNotNull(start), Preconditions.checkNotNull(end), startInclusive, endInclusive);
    }

    public String getRangeString(){
        StringBuilder builder = new StringBuilder();
        if(this.isStartInclusive()){
            builder.append('[');
        }else{
            builder.append('(');
        }
        builder.append(this.getStart());
        builder.append(", ");
        if(this.isEndInclusive()){
            builder.append(']');
        }else{
            builder.append(')');
        }
        return builder.toString();
    }
}
