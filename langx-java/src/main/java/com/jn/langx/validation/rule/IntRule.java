package com.jn.langx.validation.rule;


import com.jn.langx.util.Numbers;
import com.jn.langx.util.function.Predicate;

public class IntRule extends PredicateRule{
    public IntRule(String errorMessage) {
        super(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                if(!Numbers.isNumber(value)){
                    return false;
                }
                return Numbers.isInteger(Numbers.createInteger(value));
            }
        }, errorMessage);
    }
}
