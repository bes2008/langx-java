package com.jn.langx.util.converter;

import com.jn.langx.Converter;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.reflect.type.Primitives;

public class FloatConverter implements Converter<Object, Float> {
    public static final FloatConverter INSTANCE = new FloatConverter();

    private static final DoubleConverter doubleConverter = DoubleConverter.INSTANCE;

    @Override
    public boolean isConvertible(Class sourceClass, Class targetClass) {
        return Primitives.isFloat(targetClass);
    }

    @Override
    public Float apply(Object input) {
        Double doubleValue = doubleConverter.apply(input);
        return Numbers.convertNumberToTargetClass(doubleValue, Float.class);
    }
}
