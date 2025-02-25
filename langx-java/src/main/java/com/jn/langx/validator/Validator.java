package com.jn.langx.validator;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

import java.util.List;

public class Validator {
    private List<Rule> rules;

    public Validator() {
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public ValidationResult validate(@NonNull String value) {
        ValidationResult result = ValidationResult.ofValid();
        for (Rule rule : this.rules){
            ValidationResult r2 = rule.test(value);
            result = result.merge(r2);

            if(!result.isValid() && rule instanceof RequiredRule){
                break;
            }
        }
        return result;
    }
}
