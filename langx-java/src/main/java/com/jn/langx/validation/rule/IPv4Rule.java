package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.net.Nets;

public class IPv4Rule extends PredicateRule {
    public IPv4Rule() {
        this(null);
    }

    public IPv4Rule(String errorMessage) {
        super(Objs.useValueIfNull(errorMessage, "invalid ipv4 address"), new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return Nets.isValidIpV4Address(value);
            }
        });
    }
}
