package com.jn.langx.validator;

import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.net.Nets;

public class IPRule extends PredicateRule{
    public IPRule(String errorMessage) {
        super(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return Nets.isValidIpV4Address(value) || Nets.isValidIpV6Address(value);
            }
        }, Objs.useValueIfNull(errorMessage, "invalid ip address"));
    }
}
