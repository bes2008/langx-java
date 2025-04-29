package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Predicate;

public class BooleanRule extends PredicateRule{
    public BooleanRule(String errorMessage) {
        super(Objs.useValueIfNull(errorMessage, "must be `true` or `false`, ignore case"),
                new Predicate<String>() {
            @Override
            public boolean test(String value) {
                String v = Strings.upperCase(value);
                return "TRUE".equals(v) || "FALSE".equals(v);
            }
        });
    }
}
