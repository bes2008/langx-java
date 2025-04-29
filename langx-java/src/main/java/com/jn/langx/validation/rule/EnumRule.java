package com.jn.langx.validation.rule;

import com.jn.langx.util.enums.Enums;
import com.jn.langx.util.function.Predicate;

public class EnumRule extends PredicateRule {
    public EnumRule(String errorMessage, final Class enumClass) {
        super(errorMessage, new Predicate<String>() {
            @Override
            public boolean test(String value) {
                Object o = Enums.ofName(enumClass, value);
                return o != null;
            }
        });
    }
}
