package com.jn.langx.java8.util.function.tojava8;

import java.util.function.Consumer;

public class ConsumerAdapter<V> implements Consumer<V> {
    private com.jn.langx.util.function.Consumer<V> delegate;

    public ConsumerAdapter(com.jn.langx.util.function.Consumer<V> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void accept(V v) {
        this.delegate.accept(v);
    }
}
