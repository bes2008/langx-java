package com.jn.langx.util.struct;

import com.jn.langx.util.hash.Hashed;

public interface Reference<T> extends Hashed {
    T get();
    boolean isNull();
}
