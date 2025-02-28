package com.jn.langx.validation.rule;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.ranges.IntRange;

public class PortRangeRule extends IntRangeRule{
    public PortRangeRule() {
        this(null, new IntRange(0, 65535));
    }
    public PortRangeRule(String errorMessage) {
        this( errorMessage, new IntRange(0, 65535));
    }
    public PortRangeRule(String errorMessage, final IntRange range) {
        super(Objs.useValueIfEmpty(errorMessage, new Supplier<String, String>() {
            @Override
            public String get(String input) {
                return StringTemplates.formatWithPlaceholder("端口范围不合法: {}", range.getRangeString());
            }
        }), range );
    }
}
