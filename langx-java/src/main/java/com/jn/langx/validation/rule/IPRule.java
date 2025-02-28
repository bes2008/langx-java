package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.net.Nets;

public class IPRule extends PredicateRule {
    public IPRule(String errorMessage) {
        super(Objs.useValueIfNull(errorMessage, "invalid ip address"),
                new Predicate<String>() {
                    @Override
                    public boolean test(String value) {
                        return Nets.isValidIpV4Address(value) || Nets.isValidIpV6Address(value);
                    }
                });
    }
}
