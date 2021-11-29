package com.jn.langx.java8.util.function.fromjava8;

import com.jn.langx.util.function.Function;

public class FunctionAdapter<I, O> implements Function<I, O> {
    private java.util.function.Function<I, O> delegate;

    public FunctionAdapter(java.util.function.Function<I, O> delegate) {
        this.delegate = delegate;
    }

    @Override
    public O apply(I input) {
        return delegate.apply(input);
    }
}
