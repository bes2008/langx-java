package com.jn.langx.validator;

import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Functions;

public class RequiredRule extends PredicateRule {
    public RequiredRule(String errorMessage) {
        super(Functions.<String>notEmptyPredicate(), Objs.useValueIfEmpty(errorMessage, "Required"));
    }
}
