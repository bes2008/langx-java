package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Predicate;

public class LengthRule extends PredicateRule {
    public LengthRule(final int length){
        this(null,length);
    }
    public LengthRule(String errorMessage, final int length) {
        super(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return value.length() == length;
            }
        }, Objs.useValueIfEmpty(errorMessage, "length is not " + length));
    }
}
