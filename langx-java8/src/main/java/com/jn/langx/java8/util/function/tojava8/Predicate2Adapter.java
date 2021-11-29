package com.jn.langx.java8.util.function.tojava8;

import com.jn.langx.util.function.Predicate2;

import java.util.function.BiPredicate;

public class Predicate2Adapter<V1, V2> implements BiPredicate<V1, V2> {
    private Predicate2<V1, V2> delegate;

    public Predicate2Adapter(Predicate2<V1, V2> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean test(V1 key, V2 value) {
        return delegate.test(key, value);
    }
}
