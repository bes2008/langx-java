package com.jn.langx.util.struct;

import com.jn.langx.AbstractNameable;
import com.jn.langx.validation.rule.Rule;

import java.util.List;

public class VerifiableFieldMetadata extends AbstractNameable {
    private boolean required = true;
    private CharData invalidChars;
    private List<Rule> rules;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public CharData getInvalidChars() {
        return invalidChars;
    }

    public void setInvalidChars(CharData invalidChars) {
        this.invalidChars = invalidChars;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }
}
