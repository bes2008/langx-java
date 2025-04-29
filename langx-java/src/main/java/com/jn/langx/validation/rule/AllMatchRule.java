package com.jn.langx.validation.rule;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.function.Predicate;

import java.util.List;

public final class AllMatchRule extends AbstractRule {
    private final List<Rule> rules = Lists.newArrayList();

    public AllMatchRule(String errorMessage) {
        super(errorMessage);
    }

    public AllMatchRule(){
        this(null);
    }

    public AllMatchRule addRule(Rule rule) {
        rules.add(rule);
        return this;
    }

    public AllMatchRule addRule(Predicate<String> predicate){
       return addRule(new PredicateRule(predicate));
    }

    @Override
    protected ValidationResult doTest(final String value) {
        boolean valid = Collects.allMatch(rules, new Predicate<Rule>() {
            @Override
            public boolean test(Rule rule) {
                return rule.test(value).isValid();
            }
        });
        return valid ? ValidationResult.ofValid() : ValidationResult.ofInvalid(errorMessage);
    }
}
