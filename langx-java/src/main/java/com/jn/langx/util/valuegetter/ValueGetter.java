package com.jn.langx.util.valuegetter;

import com.jn.langx.Factory;

public interface ValueGetter<I,O> extends Factory<I,O> {
    @Override
    O get(I input);
}
