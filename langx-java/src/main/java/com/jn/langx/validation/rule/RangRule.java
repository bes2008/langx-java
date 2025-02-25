package com.jn.langx.validation.rule;

import com.jn.langx.Converter;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.ranges.Range;

class RangRule<V> extends PredicateRule{
    public RangRule(final Range<V> range, final Class<V> targetType, final Converter converter, String errorMessage) {
        super(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                if(targetType==null||targetType== String.class){
                    return range.contains((V)value);
                }
                if(converter==null){
                    return false;
                }
                if(!converter.isConvertible(String.class, targetType)){
                    return false;
                }
                V v = (V)converter.apply(value);
                return range.contains(v);
            }
        }, errorMessage);
    }
}
