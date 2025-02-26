package com.jn.langx.validation.text;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.validation.ValidateMode;
import com.jn.langx.validation.Validator;
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
        ValidationResult result = requiredRule == null ? ValidationResult.ofValid() : requiredRule.test(value);
        if (!result.isValid()) {
            return result;
        }

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
