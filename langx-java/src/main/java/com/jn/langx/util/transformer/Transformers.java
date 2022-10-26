package com.jn.langx.util.transformer;

import com.jn.langx.Transformer;
import com.jn.langx.util.function.Function;

public class Transformers {
    private Transformers(){}

    public static <I, O> Transformer<I, O> of(final Function<I, O> func) {
        return new Transformer<I, O>() {
            @Override
            public O transform(I input) {
                return func.apply(input);
            }
        };
    }
}
