package com.jn.langx;

public interface Formatter<I, O> {
    O format(I input, Object... args);
}
