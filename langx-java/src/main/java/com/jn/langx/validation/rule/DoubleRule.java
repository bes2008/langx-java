package com.jn.langx.validation.rule;

import com.jn.langx.util.Numbers;
import com.jn.langx.util.function.Predicate;

public class DoubleRule extends PredicateRule{
    public DoubleRule(String errorMessage) {
        super(errorMessage, new Predicate<String>() {
            @Override
            public boolean test(String value) {
                if(!Numbers.isNumber(value)){
                    return false;
                }
                return Numbers.isDouble(Numbers.createInteger(value));
            }
        });
    }
}
