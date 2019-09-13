package com.jn.langx.util.enums;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;

import java.util.EnumSet;

public class Enums {
    public static Enum ofName(final String name, Class targetClass) {
        Preconditions.checkTrue(targetClass.isEnum(), targetClass.getName() + " not an enum class");
        return Collects.findFirst(EnumSet.allOf(targetClass), new Predicate<Enum>() {
            @Override
            public boolean test(Enum e) {
                return e.name().equals(name);
            }
        });
    }
}
