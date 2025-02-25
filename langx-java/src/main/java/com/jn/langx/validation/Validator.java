package com.jn.langx.validation;

import com.jn.langx.annotation.NonNull;

public interface Validator<T> {
    ValidationResult validate(@NonNull T value);
}
