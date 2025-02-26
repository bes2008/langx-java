package com.jn.langx.validation.rule;

public class RequiredRule extends AbstractRule {

    public RequiredRule(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public ValidationResult doTest(String value) {
        return ValidationResult.ofValid();
    }
}
