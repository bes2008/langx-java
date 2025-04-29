package com.jn.langx.validation.rule;

import com.jn.langx.util.converter.IntegerConverter;
import com.jn.langx.util.ranges.IntRange;

public class IntRangeRule extends RangRule<Integer>{
    public IntRangeRule(String errorMessage, IntRange range) {
        super(errorMessage, range, Integer.class, IntegerConverter.INSTANCE);
    }
}
