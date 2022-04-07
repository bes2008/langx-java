package com.jn.langx.util.function.supplier;

import com.jn.langx.util.reflect.Reflects;

/**
 * @since 4.4.7
 */
public class ClassNamePrefixSupplier implements PrefixSupplier {
    @Override
    public String get(Object supplement) {
        if (supplement == null) {
            return "";
        }
        Class clazz = supplement.getClass();
        return Reflects.getSimpleClassName(clazz);
    }
}
