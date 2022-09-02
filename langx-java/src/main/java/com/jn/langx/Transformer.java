package com.jn.langx;

public interface Transformer<I,O> {
    O transform(I input);
}
