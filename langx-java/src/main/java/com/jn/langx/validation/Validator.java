package com.jn.langx.validation;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.validation.rule.ValidationResult;

public interface Validator<T> {
    void setValidateMode(ValidateMode mode);
    ValidateMode getValidateMode();
    ValidationResult validate(@NonNull T value);
}
