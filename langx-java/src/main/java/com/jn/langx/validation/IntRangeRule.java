package com.jn.langx.validation;

import com.jn.langx.util.converter.IntegerConverter;
import com.jn.langx.util.ranges.IntRange;

public class IntRangeRule extends RangRule<Integer>{
    public IntRangeRule(IntRange range, String errorMessage) {
        super(range, Integer.class, IntegerConverter.INSTANCE, errorMessage);
    }
}
