package com.jn.langx.util.converter;

import com.jn.langx.Converter;
import com.jn.langx.util.Numbers;

public class ShortConverter implements Converter<Object, Short> {
    public static final ShortConverter INSTANCE = new ShortConverter();

    private IntegerConverter integerConverter = IntegerConverter.INSTANCE;

    @Override
    public boolean isConvertible(Class sourceClass, Class targetClass) {
        return integerConverter.isConvertible(sourceClass, targetClass);
    }

    @Override
    public Short apply(Object input) {
        Integer integer = integerConverter.apply(input);
        return Numbers.convertNumberToTargetClass(integer, Short.class);
    }
}
