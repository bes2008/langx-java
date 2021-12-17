package com.jn.langx.util.function.predicate;

import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Predicate;

public class StringEqualsPredicate implements Predicate<String> {
    private boolean ignoreCase;
    private String expected;

    public StringEqualsPredicate(String expected) {
        this(expected, false);
    }

    public StringEqualsPredicate(String expected, boolean ignoreCase) {
        this.expected = expected;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean test(String value) {
        return Strings.equals(value, expected, ignoreCase);
    }
}
