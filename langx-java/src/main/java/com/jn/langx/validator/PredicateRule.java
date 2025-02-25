package com.jn.langx.validator;

import com.jn.langx.util.function.Predicate;

public class PredicateRule implements Rule{
    private Predicate<String> predicate;
    private String errorMessage;

    public PredicateRule(Predicate<String> predicate, String errorMessage) {
        this.predicate = predicate;
        this.errorMessage = errorMessage;
    }

    @Override
    public ValidationResult test(String value) {
        boolean matches = predicate.test(value);
        if (matches){
            return ValidationResult.ofValid();
        }else {
            return ValidationResult.ofInvalid(errorMessage);
        }
    }
}
