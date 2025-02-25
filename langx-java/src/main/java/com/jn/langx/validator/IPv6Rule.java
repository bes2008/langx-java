package com.jn.langx.validator;

import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.net.Nets;

public class IPv6Rule extends PredicateRule{
    public IPv6Rule(String errorMessage) {
        super(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return Nets.isValidIpV6Address(value);
            }
        }, errorMessage);
    }
}
