package com.jn.langx.validation.rule;


import com.jn.langx.util.Objs;

public class ISBNRule extends ForwardingRule {
    public ISBNRule(String errorMessage) {
        super(Objs.useValueIfEmpty(errorMessage, "invalid ISBN"),
                new AnyMatchRule(errorMessage)
                        .addRule(new ISBN13Rule())
                        .addRule(new ISBN10Rule())
                );
    }
}
