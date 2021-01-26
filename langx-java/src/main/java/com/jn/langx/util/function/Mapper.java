package com.jn.langx.util.function;

public interface Mapper<I, O> extends Function<I, O> {
    @Override
    O apply(I input);
}
