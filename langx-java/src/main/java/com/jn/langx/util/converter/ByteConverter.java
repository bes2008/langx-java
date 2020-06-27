package com.jn.langx.util.converter;

import com.jn.langx.Converter;
import com.jn.langx.util.Numbers;

public class ByteConverter implements Converter<Object, Byte> {
    public static final ByteConverter INSTANCE = new ByteConverter();
    private static final IntegerConverter integerConverter = IntegerConverter.INSTANCE;

    @Override
    public boolean isConvertible(Class sourceClass, Class targetClass) {
        return integerConverter.isConvertible(sourceClass, targetClass);
    }

    @Override
    public Byte apply(Object input) {
        Integer integer = integerConverter.apply(input);
        return Numbers.convertNumberToTargetClass(integer, Byte.class);
    }
}
