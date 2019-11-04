package com.jn.langx.util.enums;

import com.jn.langx.Delegatable;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.enums.base.EnumDelegate;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;

import java.util.EnumSet;

public class Enums {

    public static <T extends Enum<T>> T ofValue(final int value, Class<T> targetClass) {
        Preconditions.checkTrue(targetClass.isEnum(), targetClass.getName() + " not an enum class");
        return Collects.findFirst(EnumSet.<T>allOf(targetClass), new Predicate<T>() {
            @Override
            public boolean test(T e) {
                return e.ordinal() == value;
            }
        });
    }

    public static <T extends Enum<T>> T ofCode(@NonNull final Class<T> tClass, final int code) {
        T value = null;
        Preconditions.checkNotNull(tClass);
        if (Reflects.isSubClassOrEquals(Delegatable.class, tClass)) {
            EnumSet<T> enums = EnumSet.allOf(tClass);
            for (T x : enums) {
                Delegatable y = (Delegatable) x;
                Object delegateObj = y.getDelegate();
                if (delegateObj != null && delegateObj instanceof EnumDelegate) {
                    EnumDelegate delegate = (EnumDelegate) delegateObj;
                    if (delegate.getCode() == code) {
                        return x;
                    }
                }
            }
        }
        return null;
    }

    public static <T extends Enum<T>> T ofName(@NonNull final Class<T> tClass, final String name) {
        Preconditions.checkNotNull(tClass);
        if (Reflects.isSubClassOrEquals(Delegatable.class, tClass)) {
            EnumSet<T> enums = EnumSet.allOf(tClass);
            for (T x : enums) {
                Delegatable y = (Delegatable) x;
                Object delegateObj = y.getDelegate();
                if (delegateObj != null && delegateObj instanceof EnumDelegate) {
                    EnumDelegate delegate = (EnumDelegate) delegateObj;
                    if (delegate.getName().equals(name)) {
                        return x;
                    }
                }
            }
        }

        return Collects.findFirst(EnumSet.<T>allOf(tClass), new Predicate<T>() {
            @Override
            public boolean test(T e) {
                return e.name().equals(name);
            }
        });
    }

    public static <T extends Enum<T>> T ofDisplayText(@NonNull final Class<T> tClass, final String displayText) {
        Preconditions.checkNotNull(tClass);
        if (Reflects.isSubClassOrEquals(Delegatable.class, tClass)) {
            EnumSet<T> enums = EnumSet.allOf(tClass);
            for (T x : enums) {
                Delegatable y = (Delegatable) x;
                Object delegateObj = y.getDelegate();
                if (delegateObj != null && delegateObj instanceof EnumDelegate) {
                    EnumDelegate delegate = (EnumDelegate) delegateObj;
                    if (delegate.getDisplayText().equals(displayText)) {
                        return x;
                    }
                }
            }
        }
        return null;
    }
}
