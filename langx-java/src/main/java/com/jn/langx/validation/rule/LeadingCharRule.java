package com.jn.langx.validation.rule;


import com.jn.langx.util.function.Predicate;

public class LeadingCharRule extends PredicateRule {
    public LeadingCharRule(String errorMessage, final CharData charData) {
        super(errorMessage, new Predicate<String>() {
            @Override
            public boolean test(String value) {
                char leadingChar = value.charAt(0);
                return charData.getChars().contains(""+leadingChar);
            }
        } );
    }
}
