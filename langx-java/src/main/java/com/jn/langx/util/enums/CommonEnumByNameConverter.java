package com.jn.langx.util.enums;

import com.jn.langx.Converter;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.reflect.Reflects;

public class CommonEnumByNameConverter<T extends CommonEnum> implements Converter<String, T> {
    private Class enumClass;

    public CommonEnumByNameConverter(Class enumClass){
        this.enumClass = enumClass;
    }

    @Override
    public boolean isConvertible(Class sourceClass, Class targetClass) {
        return Reflects.isSubClassOrEquals(String.class, sourceClass) && targetClass==enumClass;
    }

    @Override
    public T apply(String name) {
        return (T) Enums.ofName(this.enumClass, name);
    }

    public Class<T> getEnumClass() {
        return enumClass;
    }
}
