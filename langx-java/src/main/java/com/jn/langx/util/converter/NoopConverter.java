package com.jn.langx.util.converter;

import com.jn.langx.Converter;

public class NoopConverter implements Converter {
    public static final NoopConverter INSTANCE = new NoopConverter();

    @Override
    public Object apply(Object input) {
        return input;
    }
}
