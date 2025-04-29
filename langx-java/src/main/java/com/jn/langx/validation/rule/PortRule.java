package com.jn.langx.validation.rule;


import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Predicate;

public class PortRule extends PredicateRule{
    public PortRule(String errorMessage, final int port) {
        super(errorMessage, new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return Strings.equals(value, ""+port);
            }
        });
    }
}
