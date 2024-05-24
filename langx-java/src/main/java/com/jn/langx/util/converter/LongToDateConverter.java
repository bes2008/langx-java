package com.jn.langx.util.converter;

import com.jn.langx.Converter;
import com.jn.langx.util.reflect.type.Primitives;

import java.util.Date;

public class LongToDateConverter implements Converter<Long, Date> {
    public static final LongToDateConverter INSTANCE = new LongToDateConverter();

    @Override
    public boolean isConvertible(Class sourceClass, Class targetClass) {
        return Primitives.isLong(sourceClass);
    }

    @Override
    public Date apply(Long value) {
        return new Date(value);
    }
}
