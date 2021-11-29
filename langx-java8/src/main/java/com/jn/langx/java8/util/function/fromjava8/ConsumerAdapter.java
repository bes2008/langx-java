package com.jn.langx.java8.util.function.fromjava8;

import com.jn.langx.util.function.Consumer;

public class ConsumerAdapter<V> implements Consumer<V> {
    private java.util.function.Consumer<V> delegate;

    public ConsumerAdapter(java.util.function.Consumer<V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void accept(V v) {
        this.delegate.accept(v);
    }
}
