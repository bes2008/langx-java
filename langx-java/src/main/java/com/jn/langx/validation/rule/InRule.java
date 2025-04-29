package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;

public class InRule extends PredicateRule {

    public InRule(String errorMessage, final String... candidates) {
        super(errorMessage, new Predicate<String>() {
            @Override
            public boolean test(String value) {
                if (Objs.isEmpty(candidates)) {
                    return true;
                }
                return Collects.contains(candidates, value);
            }
        });
    }
}