package com.jn.langx.validation.rule;

import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Predicate;

public class ContainsAnyRule extends PredicateRule{

    public ContainsAnyRule(String errorMessage, final String... substrings) {
        super(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return Strings.containsAny(value, substrings);
            }
        }, errorMessage);
    }
}
