package com.jn.langx.util.converter;

import com.jn.langx.Converter;

public class NoopConverter implements Converter {
    public static final NoopConverter INSTANCE = new NoopConverter();

    @Override
    public boolean isConvertible(Class sourceClass, Class targetClass) {
        return sourceClass==targetClass;
    }

    @Override
    public Object apply(Object input) {
        return input;
    }
}
