package com.jn.langx.util.enums;

import com.jn.langx.Converter;
import com.jn.langx.util.reflect.Reflects;

public class EnumByNameConverter implements Converter<String, Enum> {
    private Class enumClass;

    public EnumByNameConverter(Class enumClass){
        this.enumClass = enumClass;
    }

    @Override
    public boolean isConvertible(Class sourceClass, Class targetClass) {
        return Reflects.isSubClassOrEquals(String.class, sourceClass) && targetClass==enumClass;
    }

    @Override
    public Enum apply(String name) {
        return Enums.ofName(this.enumClass, name);
    }
}