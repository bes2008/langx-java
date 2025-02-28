package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Predicate;

public class ISBN13Rule extends ForwardingRule {
    public ISBN13Rule(){
        this(null);
    }
    public ISBN13Rule(String errorMessage) {
        super(new AllMatchRule(errorMessage)
                        .addRule(new LengthRule(13))
                        .addRule(new Predicate<String>() {
                            @Override
                            public boolean test(String isbn) {
                                int sum = 0;
                                for (int i = 0; i < isbn.length(); i++) {
                                    sum += (isbn.charAt(i) - '0') * (i % 2 == 0 ? 1 : 3);
                                }
                                return sum % 10 == 0;
                            }
                        })
                , Objs.useValueIfEmpty(errorMessage, "invalid ISBN 13"));
    }
}
