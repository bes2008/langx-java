package com.jn.langx.java8.util.function.fromjava8;

import com.jn.langx.util.function.Supplier0;

import java.util.function.Supplier;

public class Supplier0Adapter<O> implements Supplier0<O> {
    private Supplier<O> delegate;
    public Supplier0Adapter(Supplier<O> delegate){
        this.delegate = delegate;
    }

    @Override
    public O get() {
        return delegate.get();
    }
}
