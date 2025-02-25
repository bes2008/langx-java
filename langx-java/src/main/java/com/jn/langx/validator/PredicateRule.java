package com.jn.langx.validator;

import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Predicate;

public class PredicateRule implements Rule {
    private Predicate<String> predicate;
    private String errorMessage;

    public PredicateRule(Predicate<String> predicate, String errorMessage) {
        this.predicate = predicate;
        this.errorMessage = errorMessage;
    }

    @Override
    public ValidationResult test(String value) {
        if (Strings.isEmpty(value)) {
            return ValidationResult.ofInvalid(errorMessage);
        }
        return predicate.test(value.trim()) ? ValidationResult.ofValid() : ValidationResult.ofInvalid(errorMessage);
    }
}
