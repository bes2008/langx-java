package com.jn.langx.validation;

import com.jn.langx.validation.rule.ValidationResult;

import java.util.List;

public class MethodParametersValidator implements Validator<Object[]> {
    private List<Validator> validators;
    private ValidateMode validateMode = ValidateMode.QUICK_FAIL;
    @Override
    public void setValidateMode(ValidateMode mode) {
        this.validateMode = mode;
    }

    @Override
    public ValidateMode getValidateMode() {
        return this.validateMode;
    }

    @Override
    public ValidationResult validate(Object[] value) {
        return null;
    }
}
