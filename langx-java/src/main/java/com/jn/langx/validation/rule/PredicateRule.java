package com.jn.langx.validation.rule;

import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Predicate;

public class PredicateRule extends AbstractRule {
    private Predicate<String> predicate;
    public PredicateRule(Predicate<String> predicate) {
        this(null, predicate);
    }
    public PredicateRule(String errorMessage, Predicate<String> predicate) {
        super(errorMessage);
        this.predicate = predicate;
    }

    @Override
    public ValidationResult doTest(String value) {
        if (Strings.isEmpty(value)) {
            return ValidationResult.ofInvalid(errorMessage);
        }
        return predicate.test(value.trim()) ? ValidationResult.ofValid() : ValidationResult.ofInvalid(errorMessage);
    }
}
