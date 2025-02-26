package com.jn.langx.validation;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.validation.rule.RequiredRule;
import com.jn.langx.validation.rule.Rule;
import com.jn.langx.validation.rule.ValidationResult;

import java.util.List;

public class TextValidator implements Validator<String> {
    private List<Rule> rules = Lists.newArrayList();
    private RequiredRule requiredRule;
    private ValidateMode validateMode = ValidateMode.VALIDATE_ALL;

    public TextValidator() {
    }

    public void setRequiredRule(RequiredRule requiredRule) {
        this.requiredRule = requiredRule;
    }

    @Override
    public void setValidateMode(ValidateMode mode) {
        this.validateMode = mode;
    }

    @Override
    public ValidateMode getValidateMode() {
        return validateMode;
    }

    public void setRules(List<Rule> rules) {
        for (Rule rule : rules) {
            if (rule instanceof RequiredRule) {
                this.requiredRule = (RequiredRule) rule;
            } else {
                this.rules.add(rule);
            }
        }
    }

    public ValidationResult validate(@NonNull String value) {
        if(Strings.isEmpty(value)){
            return requiredRule == null ? ValidationResult.ofValid() : requiredRule.test(value);
        }
        ValidationResult result =  ValidationResult.ofValid();
        for (Rule rule : this.rules) {
            ValidationResult r2 = rule.test(value);
            result = result.merge(r2);
            if (!result.isValid() && validateMode == ValidateMode.QUICK_FAIL) {
                return result;
            }
        }
        return result;
    }
}
