package com.jn.langx.util.enums;

import com.jn.langx.Converter;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.reflect.Reflects;

public class CommonEnumByNameConverter implements Converter<String, CommonEnum> {
    private Class enumClass;

    public CommonEnumByNameConverter(Class enumClass){
        this.enumClass = enumClass;
    }

    @Override
    public boolean isConvertible(Class sourceClass, Class targetClass) {
        return Reflects.isSubClassOrEquals(String.class, sourceClass) && targetClass==enumClass;
    }

    @Override
    public CommonEnum apply(String name) {
        return (CommonEnum) Enums.ofName(this.enumClass, name);
    }
}
