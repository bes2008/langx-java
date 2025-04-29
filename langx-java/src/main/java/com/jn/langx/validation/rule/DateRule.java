package com.jn.langx.validation.rule;

import com.jn.langx.util.Dates;
import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Predicate;

import java.util.Date;

public class DateRule extends PredicateRule {
    public DateRule(String errorMessage, final String pattern) {
        super(Objs.useValueIfNull(errorMessage, "Invalid date string"),
                new Predicate<String>() {
                    @Override
                    public boolean test(String value) {
                        try {
                            Date parsed = Dates.parse(value, pattern);
                            return parsed != null;
                        } catch (Throwable e) {
                            return false;
                        }
                    }
                });
    }
}
