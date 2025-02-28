package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Validations;
import com.jn.langx.util.function.Predicate;

public class Rfc1123DomainNameRule extends PredicateRule {

    public Rfc1123DomainNameRule() {
        this(null);
    }

    public Rfc1123DomainNameRule(String errorMessage) {
        super(Objs.useValueIfNull(errorMessage, "Invalid RFC1123 hostname"),
                new Predicate<String>() {
                    @Override
                    public boolean test(String value) {
                        return Validations.isValidRFC1123Hostname(value);
                    }
                });
    }
}
