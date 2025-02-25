package com.jn.langx.validator;

public interface Rule {
    ValidationResult test(String value);
}
