package com.jn.langx.util.function.predicate.matcher;

import com.jn.langx.Matcher;
import com.jn.langx.util.function.Predicate;

public class MatcherPredicateAdapter<I> implements Predicate<I> {
    private Matcher<I, Boolean> matcher;

    public MatcherPredicateAdapter(Matcher<I, Boolean> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean test(I value) {
        return matcher.matches(value);
    }
}
