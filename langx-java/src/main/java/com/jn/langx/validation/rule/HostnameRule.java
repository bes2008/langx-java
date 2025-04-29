package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;

public class HostnameRule extends ForwardingRule {
    public HostnameRule(String errorMessage) {
        super(Objs.useValueIfNull(errorMessage, "invalid hostname"),
                new AnyMatchRule(errorMessage)
                        .addRule(new IPv4Rule())
                        .addRule(new IPv6Rule())
                        .addRule(new Rfc1123DomainNameRule()));
    }
}
