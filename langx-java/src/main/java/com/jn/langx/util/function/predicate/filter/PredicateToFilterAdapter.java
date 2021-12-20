package com.jn.langx.util.function.predicate.filter;

import com.jn.langx.Filter;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

public class PredicateToFilterAdapter<E> implements Filter<E> {
    private Predicate<E> predicate;

    public PredicateToFilterAdapter(Predicate<E> predicate) {
        if (predicate == null) {
            this.predicate = Functions.truePredicate();
        } else {
            this.predicate = predicate;
        }
    }

    @Override
    public boolean accept(E e) {
        return predicate.test(e);
    }
}
