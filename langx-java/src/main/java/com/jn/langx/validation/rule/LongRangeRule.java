package com.jn.langx.validation.rule;

import com.jn.langx.util.converter.LongConverter;
import com.jn.langx.util.ranges.LongRange;

public class LongRangeRule extends RangRule<Long>{
    public LongRangeRule(String errorMessage, LongRange range) {
        super(errorMessage, range, Long.class, LongConverter.INSTANCE);
    }
}
