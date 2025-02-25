package com.jn.langx.validator;

import com.jn.langx.util.Dates;
import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Predicate;

import java.util.Date;

public class DateStringRule extends PredicateRule{
    public DateStringRule(final String pattern, String errorMessage) {
        super(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                try {
                    Date parsed = Dates.parse(value, pattern);
                    return parsed != null;
                }catch (Throwable e){
                    return false;
                }
            }
        }, Objs.useValueIfNull(errorMessage, "Invalid date string"));
    }
}
