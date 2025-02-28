package com.jn.langx.validation.rule;

import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.Objs;

public class ISBN10Rule extends ForwardingRule {
    public ISBN10Rule() {
        this(null);
    }
    public ISBN10Rule(String errorMessage) {
        super(new AllMatchRule(errorMessage)
                .addRule(new LengthRule(10)).addRule(new Predicate<String>() {
                    @Override
                    public boolean test(String isbn) {
                        int sum = 0;
                        for (int i = 0; i < isbn.length() - 1; i++) {
                            sum += (isbn.charAt(i) - '0') * (10 - i);
                        }
                        return (sum + (isbn.charAt(9) == 'X' ? 10 : isbn.charAt(9) - '0')) % 11 == 0;
                    }
                }), Objs.useValueIfEmpty(errorMessage, "invalid ISBN 10"));
    }
}
