package com.jn.langx.util.enums;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.function.Predicate;

import java.util.EnumSet;
import java.util.List;

public final class JvmEnumGetter implements EnumGetter {

    @Override
    public List<Class> applyTo() {
        return Lists.<Class>newArrayList(Enum.class);
    }

    @Override
    public Enum getByName(Class tClass, final String name) {
        Enum t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<Enum>() {
            @Override
            public boolean test(Enum e) {
                return Objs.equals(e.name(), name);
            }
        });
        return t;
    }

    @Override
    public Enum getByCode(Class tClass, final int code) {
        Enum t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<Enum>() {
            @Override
            public boolean test(Enum e) {
                return e.ordinal() == code;
            }
        });
        return t;
    }

    @Override
    public Enum getByDisplayText(Class tClass, final String displayText) {
        Enum t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<Enum>() {
            @Override
            public boolean test(Enum e) {
                return Objs.equals(e.name(), displayText);
            }
        });
        return t;
    }

    @Override
    public Enum getByToString(Class tClass, final String toString) {
        Enum t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<Enum>() {
            @Override
            public boolean test(Enum e) {
                return Objs.equals(e.toString(), toString);
            }
        });
        return t;
    }

    @Override
    public String getName(Enum e) {
        return e.name();
    }

    @Override
    public String getDisplayText(Enum e) {
        return e.name();
    }

    @Override
    public int getCode(Enum e) {
        return e.ordinal();
    }
}
