package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Validations;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.net.Nets;

public class HostnameRule extends PredicateRule{
    public HostnameRule(String errorMessage) {
        super(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return Nets.isValidIpV4Address(value) || Nets.isValidIpV6Address(value) || Validations.isValidRFC1123Hostname(value);
            }
        }, Objs.useValueIfNull(errorMessage, "invalid hostname"));
    }
}
