package com.jn.langx.util.converter;

import com.jn.langx.Converter;
import com.jn.langx.util.BooleanEvaluator;
import com.jn.langx.util.reflect.type.Primitives;

@SuppressWarnings("ALL")
public class BooleanConverter implements Converter<Object, Boolean> {
    private static final BooleanEvaluator booleanEvaluator = BooleanEvaluator.createFalseEvaluator(false, true, new Object[]{"false", 0, false, "off", 'n', "no"});
    public static final BooleanConverter INSTANCE = new BooleanConverter();

    @Override
    public boolean isConvertible(Class sourceClass, Class targetClass) {
        return Primitives.isBoolean(targetClass);
    }

    @Override
    public Boolean apply(Object input) {
        return booleanEvaluator.evalTrue(input);
    }
}