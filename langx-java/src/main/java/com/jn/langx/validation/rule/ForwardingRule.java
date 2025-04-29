package com.jn.langx.validation.rule;

import com.jn.langx.util.Strings;

public class ForwardingRule extends AbstractRule{
    private Rule rule;

    public ForwardingRule(Rule rule) {
        this(null, rule);
    }

    public ForwardingRule(String errorMessage, Rule rule) {
        super(errorMessage);
        this.rule = rule;
    }

    @Override
    protected ValidationResult doTest(String value) {
        ValidationResult result = rule.test(value);
        if(!result.isValid() && Strings.isNotEmpty(this.errorMessage)){
            return ValidationResult.ofInvalid(this.errorMessage);
        }
        return result;
    }
}
