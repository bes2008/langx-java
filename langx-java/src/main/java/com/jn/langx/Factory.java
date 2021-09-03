package com.jn.langx;

import com.jn.langx.util.function.Supplier;

public interface Factory<I, O> extends Supplier<I, O> {
    @Override
    O get(I input);
}
