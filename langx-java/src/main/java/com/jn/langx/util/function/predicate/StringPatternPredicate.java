package com.jn.langx.util.function.predicate;

import com.jn.langx.util.function.Predicate;

import java.util.regex.Pattern;

public class StringPatternPredicate implements Predicate<String> {
    private Pattern pattern;

    public StringPatternPredicate(String regexp, int flags) {
        pattern = Pattern.compile(regexp, flags);
    }


    public StringPatternPredicate(String regexp) {
        this(regexp, 0);
    }

    @Override
    public boolean test(String value) {
        return pattern.matcher(value).matches();
    }
}