package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Predicate;

public class NotRule extends PredicateRule {
    public NotRule(String errorMessage, final Rule rule) {
        super(Objs.useValueIfEmpty(errorMessage, "the value is not valid"),
                new Predicate<String>() {
            @Override
            public boolean test(String value) {
                ValidationResult r = rule.test(value);
                return !r.isValid();
            }
        });
    }
}
