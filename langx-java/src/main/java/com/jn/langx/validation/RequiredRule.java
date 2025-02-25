package com.jn.langx.validation;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;

public class RequiredRule implements Rule {
    private String errorMessage;

    public RequiredRule(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public ValidationResult test(String value) {
        return Strings.isNotEmpty(value) ? ValidationResult.ofValid() : ValidationResult.ofInvalid(Objs.useValueIfEmpty(errorMessage, "Required"));
    }
}
