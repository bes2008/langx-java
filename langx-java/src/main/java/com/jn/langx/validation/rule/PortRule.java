package com.jn.langx.validation.rule;

import com.jn.langx.util.ranges.IntRange;

public class PortRule extends IntRangeRule {
    public PortRule(String errorMessage, IntRange range) {
        super(errorMessage, range);
    }

    public PortRule(String errorMessage) {
        super(errorMessage, new IntRange(0, 65535));
    }
}
