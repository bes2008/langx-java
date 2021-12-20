package com.jn.langx.util.function.predicate.filter;

import com.jn.langx.Filter;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

public class FilterToPredicateAdapter<E> implements Predicate<E> {
    private Filter<E> filter;

    public FilterToPredicateAdapter(Filter<E> filter) {
        if (filter == null) {
            this.filter = Functions.trueFilter();
        } else {
            this.filter = filter;
        }
    }

    @Override
    public boolean test(E value) {
        return this.filter.accept(value);
    }
}
