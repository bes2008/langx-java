package com.jn.langx.util.enums;

import com.jn.langx.Delegatable;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Objects;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;

import java.util.EnumSet;

public class Enums {

    public static <T extends Enum<T>> T ofValue(final int value, Class<T> targetClass) {
        Preconditions.checkTrue(targetClass.isEnum(), targetClass.getName() + " not an enum class");
        return Collects.findFirst(EnumSet.allOf(targetClass), new Predicate<T>() {
            @Override
            public boolean test(T e) {
                return e.ordinal() == value;
            }
        });
    }

    public static <T extends Enum<T>> T ofCode(@NonNull final Class<T> tClass, final int code) {
        Preconditions.checkNotNull(tClass);
        if (Reflects.isSubClass(CommonEnum.class, tClass)) {
            return Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    CommonEnum y = (CommonEnum) e;
                    return y.getCode() == code;
                }
            });
        } else {
            return Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    return e.ordinal() == code;
                }
            });
        }
    }

    public static <T extends Enum<T>> T ofName(@NonNull final Class<T> tClass, final String name) {
        Preconditions.checkNotNull(tClass);
        if (Reflects.isSubClass(CommonEnum.class, tClass)) {
            return Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    CommonEnum y = (CommonEnum) e;
                    return y.getName().equals(name);
                }
            });
        }
        return Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
            @Override
            public boolean test(T e) {
                return e.name().equals(name);
            }
        });
    }

    public static <T extends Enum<T>> T ofDisplayText(@NonNull final Class<T> tClass, final String displayText) {
        Preconditions.checkNotNull(tClass);
        if (Reflects.isSubClass(CommonEnum.class, tClass)) {
            return Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    CommonEnum y = (CommonEnum) e;
                    return y.getDisplayText().equals(displayText);
                }
            });
        } else {
            return Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    return e.name().equals(displayText);
                }
            });
        }
    }

    public static <T extends Enum<T>> T inferEnum(Class<T> targetClass, String text) {
        if (targetClass.isEnum() && Reflects.isSubClassOrEquals(Enum.class, targetClass)) {
            T v = ofDisplayText(targetClass, text);
            if (v == null) {
                return ofName(targetClass, text);
            }
        }
        return null;
    }

    public static <T extends Enum<T>> T ofField(Class<T> targetClass, final String field, final Object value) {
        Preconditions.checkNotNull(targetClass);
        Preconditions.checkNotNull(field);
        if (targetClass.isEnum() && Reflects.isSubClassOrEquals(Enum.class, targetClass)) {
            return Collects.findFirst(EnumSet.allOf(targetClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    Object fieldValue = Reflects.getAnyFieldValue(e, field, true, false);
                    return Objects.deepEquals(fieldValue, value);
                }
            });
        }
        throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("{} not a enum class", Reflects.getFQNClassName(targetClass)));
    }

    public static <T extends Enum<T>> T ofField(Class<T> targetClass, final String field, final Object value, Predicate<T> predicate) {
        Preconditions.checkNotNull(targetClass);
        Preconditions.checkNotNull(field);
        if (predicate == null) {
            return ofField(targetClass, field, value);
        }
        if (targetClass.isEnum() && Reflects.isSubClassOrEquals(Enum.class, targetClass)) {
            return Collects.findFirst(EnumSet.allOf(targetClass), predicate);
        }
        throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("{} not a enum class", Reflects.getFQNClassName(targetClass)));
    }
}
