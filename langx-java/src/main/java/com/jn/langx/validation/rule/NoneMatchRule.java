package com.jn.langx.validation.rule;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.function.Predicate;

import java.util.List;

public final class NoneMatchRule extends AbstractRule {
    private final List<Rule> rules = Lists.newArrayList();

    public NoneMatchRule(String errorMessage) {
        super(errorMessage);
    }

    public NoneMatchRule(){
        this(null);
    }
    public NoneMatchRule addRule(Rule rule) {
        rules.add(rule);
        return this;
    }

    public NoneMatchRule addRule(Predicate<String> predicate){
        return addRule(new PredicateRule(predicate, null));
    }

    @Override
    protected ValidationResult doTest(final String value) {
        boolean valid = Collects.noneMatch(rules, new Predicate<Rule>() {
            @Override
            public boolean test(Rule rule) {
                return rule.test(value).isValid();
            }
        });
        return valid ? ValidationResult.ofValid() : ValidationResult.ofInvalid(errorMessage);
    }
}