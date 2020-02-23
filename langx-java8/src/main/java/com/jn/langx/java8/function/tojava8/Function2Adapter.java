package com.jn.langx.java8.function.tojava8;

import com.jn.langx.util.function.Function2;

import java.util.function.BiFunction;

public class Function2Adapter<I1, I2, O> implements BiFunction<I1, I2, O> {
    private Function2<I1, I2, O> delegate;

    public Function2Adapter(Function2<I1, I2, O> delegate) {
        this.delegate = delegate;
    }

    @Override
    public O apply(I1 input1, I2 input2) {
        return delegate.apply(input1, input2);
    }
}
