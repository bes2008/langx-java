package com.jn.langx.util.converter;

import com.jn.langx.Converter;
import com.jn.langx.exception.ValueConvertException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.Primitives;

import java.util.Date;

public class DoubleConverter implements Converter<Object, Double> {
    public static final DoubleConverter INSTANCE = new DoubleConverter();

    @Override
    public boolean isConvertible(Class sourceClass, Class targetClass) {
        if(!Primitives.isDouble(targetClass) ||!Primitives.isFloat(targetClass)){
            return false;
        }

        if(sourceClass==Boolean.class || sourceClass==String.class ){
            return true;
        }

        if(Reflects.isSubClass(Number.class, sourceClass)){
            return true;
        }

        return false;
    }

    @Override
    public Double apply(Object input) {
        if (input == null) {
            return 0D;
        }
        if (input instanceof Boolean) {
            return (Boolean) input ? 1D : 0D;
        }
        if (input instanceof Number) {
            return Numbers.convertNumberToTargetClass((Number) input, Double.class);
        }
        if (input instanceof String) {
            Number number = Numbers.createNumber(input.toString());
            return Numbers.convertNumberToTargetClass(number, Double.class);
        }
        throw new ValueConvertException(StringTemplates.formatWithPlaceholder("Can't cast {} to java.lang.Double", input));
    }
}
