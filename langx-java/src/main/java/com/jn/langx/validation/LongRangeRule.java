package com.jn.langx.validation;

import com.jn.langx.util.converter.LongConverter;
import com.jn.langx.util.ranges.LongRange;

public class LongRangeRule extends RangRule<Long>{
    public LongRangeRule(LongRange range, String errorMessage) {
        super(range, Long.class, LongConverter.INSTANCE, errorMessage);
    }
}
