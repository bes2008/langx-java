package com.jn.langx.util.function.mapper;

import com.jn.langx.util.function.Mapper;

public interface StringLiteralMapper<V> extends Mapper<String,V> {
    @Override
    V apply(String value);
}
