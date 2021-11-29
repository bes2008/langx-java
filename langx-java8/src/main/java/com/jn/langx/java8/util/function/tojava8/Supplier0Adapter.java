package com.jn.langx.java8.util.function.tojava8;

import com.jn.langx.util.function.Supplier0;

import java.util.function.Supplier;

public class Supplier0Adapter<O> implements Supplier<O> {
    private Supplier0<O> delegate;
    public Supplier0Adapter(Supplier0<O> delegate){
        this.delegate = delegate;
    }

    @Override
    public O get() {
        return delegate.get();
    }
}
