package com.jn.langx.java8.function.fromjava8;

import com.jn.langx.util.function.Predicate;

public class PredicateAdapter<V> implements Predicate<V> {
    private java.util.function.Predicate<V> delegate;

    public PredicateAdapter(java.util.function.Predicate<V> predicate) {
        this.delegate = predicate;
    }

    @Override
    public boolean test(V value) {
        return delegate.test(value);
    }
}
