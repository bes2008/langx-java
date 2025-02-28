package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Predicate;

public class ISBN13Rule extends ForwardingRule {
    public ISBN13Rule() {
        this(null);
    }

    public ISBN13Rule(String errorMessage) {
        super(Objs.useValueIfEmpty(errorMessage, "invalid ISBN 13"),
                new AllMatchRule(errorMessage)
                        .addRule(
                                new AnyMatchRule()
                                        .addRule(new LengthRule(13))     // 不带连接符时，长度为10
                                        .addRule(new LengthRule(13 + 4)) // 带连接符时，长度为 13+4。因为有4个是连接符
                        )
                        .addRule(new RegexpRule("invalid ISBN 10", "^[0-9]+([-]?[0-9]+){4}"))
                        .addRule(new Predicate<String>() {
                            @Override
                            public boolean test(String isbn) {
                                if (isbn.length() != 13) {
                                    isbn = isbn.replace("-", "");
                                }
                                int sum = 0;
                                for (int i = 0; i < isbn.length(); i++) {
                                    sum += (isbn.charAt(i) - '0') * (i % 2 == 0 ? 1 : 3);
                                }
                                return sum % 10 == 0;
                            }
                        })
        );
    }
}
