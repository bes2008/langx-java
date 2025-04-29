package com.jn.langx.validation.rule;

/**
 * @since 5.5.0
 */
public interface Rule {
    ValidationResult test(String value);
}
