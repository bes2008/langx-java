package com.jn.langx.parser;

import com.jn.langx.annotation.Nullable;

public interface Parser<I, O> {
    @Nullable
    O parse(I input);
}
