package com.jn.langx.parser;

public interface Parser<I, O> {
    O parse(I input);
}
