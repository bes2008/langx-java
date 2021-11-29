package com.jn.langx.java8.util.function.fromjava8;

import com.jn.langx.util.function.Function2;

import java.util.function.BiFunction;

public class Function2Adapter<I1, I2, O> implements Function2<I1, I2, O> {
    private BiFunction<I1, I2, O> delegate;

    public Function2Adapter(BiFunction<I1, I2, O> delegate) {
        this.delegate = delegate;
    }

    @Override
    public O apply(I1 input1, I2 input2) {
        return delegate.apply(input1, input2);
    }
}
