package com.jn.langx.util.converter;

import com.jn.langx.Converter;
import com.jn.langx.exception.ValueConvertException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Numbers;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.type.Primitives;

public class IntegerConverter implements Converter<Object, Integer> {
    public static final IntegerConverter INSTANCE = new IntegerConverter();


    @Override
    public boolean isConvertible(Class sourceClass, Class targetClass) {
        if (Primitives.isIntegerCompatible(targetClass)) {
            if (Primitives.isPrimitiveOrPrimitiveWrapperType(sourceClass)) {
                return true;
            }
            if(String.class==sourceClass){
                return true;
            }
            if (sourceClass.isEnum() || Reflects.isSubClassOrEquals(CommonEnum.class, sourceClass)) {
                return true;
            }
            return Primitives.isChar(sourceClass);
        }
        return false;
    }

    @Override
    public Integer apply(Object input) {
        if (input == null) {
            return 0;
        }
        if (input instanceof Boolean) {
            return (Boolean) input ? 1 : 0;
        }
        if (input instanceof Number) {
            return Numbers.convertNumberToTargetClass((Number) input, Integer.class);
        }
        if (input instanceof String) {
            Number number = Numbers.createNumber(input.toString());
            return Numbers.convertNumberToTargetClass(number, Integer.class);
        }
        if (input instanceof Character) {
            return (Character) input - 48;
        }
        if(input instanceof CommonEnum){
            return ((CommonEnum)input).getCode();
        }
        if(input instanceof Enum){
            return ((Enum)input).ordinal();
        }
        throw new ValueConvertException(StringTemplates.formatWithPlaceholder("Can't cast {} to java.lang.Integer", input));
    }
}
