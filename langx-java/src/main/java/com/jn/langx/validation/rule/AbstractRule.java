package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;

public abstract class AbstractRule implements Rule{
    protected String errorMessage;
    public AbstractRule(String errorMessage){
        this.errorMessage = errorMessage;
    }

    @Override
    public final ValidationResult test(String value) {
        if(Strings.isEmpty(value)){
            return ValidationResult.ofInvalid(Objs.useValueIfEmpty(errorMessage, "Value is required"));
        }
        return doTest(value);
    }

    protected abstract ValidationResult doTest(String value);

}
