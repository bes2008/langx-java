package com.jn.langx.validation.rule;

import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.Objs;

public class ISBN10Rule extends ForwardingRule {
    public ISBN10Rule() {
        this(null);
    }

    public ISBN10Rule(String errorMessage) {
        super(Objs.useValueIfEmpty(errorMessage, "invalid ISBN 10"), new AllMatchRule(errorMessage)
                .addRule(
                        new AnyMatchRule()
                                .addRule(new LengthRule(10))     // 不带连接符时，长度为10
                                .addRule(new LengthRule(10 + 3)) // 带连接符时，长度为 13，有3个是连接符
                )
                .addRule(new RegexpRule("invalid ISBN 10", "^[0-9]+([-]?[0-9]+){2}([-]?[0-9]*[0-9X])$"))
                .addRule(new Predicate<String>() {
                    @Override
                    public boolean test(String isbn) {
                        if (isbn.length() != 10) {
                            isbn = isbn.replace("-", "");
                        }
                        if (isbn.length() != 10) {
                            return false;
                        }
                        int sum = 0;
                        for (int i = 0; i < isbn.length() - 1; i++) {
                            sum += (isbn.charAt(i) - '0') * (10 - i);
                        }
                        return (sum + (isbn.charAt(9) == 'X' ? 10 : isbn.charAt(9) - '0')) % 11 == 0;
                    }
                }));
    }
}
