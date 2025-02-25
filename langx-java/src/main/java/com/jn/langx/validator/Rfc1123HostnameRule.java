package com.jn.langx.validator;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Validations;
import com.jn.langx.util.function.Predicate;

public class Rfc1123HostnameRule extends PredicateRule{
    public Rfc1123HostnameRule(String errorMessage) {
        super(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return Validations.isValidRFC1123Hostname(value);
            }
        }, Objs.useValueIfNull(errorMessage, "Invalid RFC1123 hostname"));
    }
}
