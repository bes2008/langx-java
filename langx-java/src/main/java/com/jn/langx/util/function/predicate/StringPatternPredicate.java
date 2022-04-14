package com.jn.langx.util.function.predicate;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;

import java.util.regex.Pattern;

public class StringPatternPredicate implements Predicate<String> {
    private Regexp regexp;

    public StringPatternPredicate(Pattern pattern) {
        Preconditions.checkNotNull(pattern);
        this.regexp = Regexps.createRegexp(pattern);
    }

    public StringPatternPredicate(String regexp, int flags) {
        this(Pattern.compile(regexp, flags));
    }


    public StringPatternPredicate(String regexp) {
        this(regexp, 0);
    }

    @Override
    public boolean test(String value) {
        return regexp.matcher(value).matches();
    }
}
