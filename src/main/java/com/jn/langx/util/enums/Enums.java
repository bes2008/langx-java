package com.jn.langx.util.enums;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;

import java.util.EnumSet;

public class Enums {
    public static <E extends Enum> E ofName(final String name, Class<E> targetClass) {
        Preconditions.checkTrue(targetClass.isEnum(), targetClass.getName() + " not an enum class");
        return Collects.findFirst(EnumSet.allOf(targetClass), new Predicate<E>() {
            @Override
            public boolean test(E e) {
                return e.name().equals(name);
            }
        });
    }
}
