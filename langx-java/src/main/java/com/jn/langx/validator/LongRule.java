package com.jn.langx.validator;

import com.jn.langx.util.Numbers;
import com.jn.langx.util.function.Predicate;

public class LongRule extends PredicateRule{
    public LongRule(String errorMessage) {
        super(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                if(!Numbers.isNumber(value)){
                    return false;
                }
                return Numbers.isLong(Numbers.createNumber(value));
            }
        }, errorMessage);
    }
}