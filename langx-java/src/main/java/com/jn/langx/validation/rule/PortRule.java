package com.jn.langx.validation.rule;

import com.jn.langx.util.ranges.IntRange;

public class PortRule extends IntRangeRule {
    public PortRule(IntRange range, String errorMessage) {
        super(range, errorMessage);
    }

    public PortRule(String errorMessage) {
        super(new IntRange(0, 65535), errorMessage);
    }
}
