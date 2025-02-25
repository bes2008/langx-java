package com.jn.langx.validation;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.validation.rule.ValidationResult;

public interface Validator<T> {
    ValidationResult validate(@NonNull T value);
}
