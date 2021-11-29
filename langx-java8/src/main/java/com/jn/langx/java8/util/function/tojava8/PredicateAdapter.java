package com.jn.langx.java8.util.function.tojava8;

import java.util.function.Predicate;

public class PredicateAdapter<V> implements Predicate<V> {
    private com.jn.langx.util.function.Predicate<V> delegate;

    public PredicateAdapter(com.jn.langx.util.function.Predicate<V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean test(V v) {
        return delegate.test(v);
    }
}
