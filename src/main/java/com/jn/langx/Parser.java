package com.jn.langx;

public interface Parser<I, O> {
    O parse(I input);
}
