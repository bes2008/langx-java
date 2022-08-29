package com.jn.langx.util.function.predicate.matcher;

import com.jn.langx.Matcher;
import com.jn.langx.util.function.Predicate;

public class PredicateMatcherAdapter<E> implements Matcher<E, Boolean> {
    private Predicate<E> predicate;

    public PredicateMatcherAdapter(Predicate<E> predicate) {
        this.predicate = predicate;
    }

    @Override
    public Boolean matches(E e) {
        return predicate.test(e);
    }
}
