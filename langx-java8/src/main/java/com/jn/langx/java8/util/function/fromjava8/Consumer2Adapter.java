package com.jn.langx.java8.util.function.fromjava8;

import com.jn.langx.util.function.Consumer2;

import java.util.function.BiConsumer;

public class Consumer2Adapter<I1, I2> implements Consumer2<I1, I2> {
    private BiConsumer<I1, I2> delegate;

    public Consumer2Adapter(BiConsumer<I1, I2> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void accept(I1 key, I2 value) {
        this.delegate.accept(key, value);
    }
}
