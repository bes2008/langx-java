package com.jn.langx.util.enums;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Objects;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Supplier0;
import com.jn.langx.util.reflect.Reflects;

import java.util.EnumSet;
import java.util.List;

public class Enums {

    /**
     * 基于 ordinal
     *
     * @param value
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> T ofValue(final int value, Class<T> targetClass) {
        Preconditions.checkTrue(targetClass.isEnum(), targetClass.getName() + " not an enum class");
        return Collects.findFirst(EnumSet.allOf(targetClass), new Predicate<T>() {
            @Override
            public boolean test(T e) {
                return e.ordinal() == value;
            }
        });
    }

    /**
     * 基于 code属性，或者 ordinal
     *
     * @param tClass
     * @param code
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> T ofCode(@NonNull final Class<T> tClass, final int code) {
        Preconditions.checkNotNull(tClass);
        T t = null;
        if (Reflects.isSubClass(CommonEnum.class, tClass)) {
            t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    CommonEnum y = (CommonEnum) e;
                    return y.getCode() == code;
                }
            });
        }
        if (t == null) {
            t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    return e.ordinal() == code;
                }
            });
        }
        return t;
    }

    /**
     * 基于name
     *
     * @param tClass
     * @param name
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> T ofName(@NonNull final Class<T> tClass, final String name) {
        Preconditions.checkNotNull(tClass);
        T t = null;
        if (Reflects.isSubClass(CommonEnum.class, tClass)) {
            t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    CommonEnum y = (CommonEnum) e;
                    return y.getName().equals(name);
                }
            });
        }
        if (t == null) {
            t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    return e.name().equals(name);
                }
            });
        }

        return t;
    }

    public static <T extends Enum<T>> T ofToString(@NonNull final Class<T> tClass, final String value) {
        Preconditions.checkNotNull(tClass);
        T t = null;
        if (Reflects.isSubClass(CommonEnum.class, tClass)) {
            t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    CommonEnum y = (CommonEnum) e;
                    return y.toString().equals(value);
                }
            });
        }
        if (t == null) {
            t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    return e.toString().equals(value);
                }
            });
        }

        return t;
    }

    public static <T extends Enum<T>> T ofDisplayText(@NonNull final Class<T> tClass, final String displayText) {
        Preconditions.checkNotNull(tClass);
        T t = null;
        if (Reflects.isSubClass(CommonEnum.class, tClass)) {
            t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    CommonEnum y = (CommonEnum) e;
                    return y.getDisplayText().equals(displayText);
                }
            });
        }
        if (t == null) {
            t = Collects.findFirst(EnumSet.allOf(tClass), new Predicate<T>() {
                @Override
                public boolean test(T e) {
                    return e.name().equals(displayText);
                }
            });
        }
        return t;
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
        return ofField(targetClass, field, value, null);
    }

    public static <T extends Enum<T>> T ofField(Class<T> targetClass, final String field, final Supplier0<Object> valueSupplier) {
        return ofField(targetClass, field, valueSupplier, null);
    }

    public static <T extends Enum<T>> T ofField(Class<T> targetClass, final String field, final Object value, Predicate<T> predicate) {
        return ofField(targetClass, field, new Supplier0<Object>() {
            @Override
            public Object get() {
                return value;
            }
        }, predicate);
    }

    public static <T extends Enum<T>> T ofField(Class<T> targetClass, final String field, final Supplier0<Object> valueSupplier, Predicate<T> predicate) {
        Preconditions.checkNotNull(targetClass);
        Preconditions.checkNotNull(field);
        if (targetClass.isEnum() && Reflects.isSubClassOrEquals(Enum.class, targetClass)) {
            if (predicate == null) {
                return Collects.findFirst(EnumSet.allOf(targetClass), new Predicate<T>() {
                    @Override
                    public boolean test(T e) {
                        Object fieldValue = Reflects.getAnyFieldValue(e, field, true, false);
                        Object expectedValue = valueSupplier == null ? null : valueSupplier.get();
                        return Objects.deepEquals(fieldValue, expectedValue);
                    }
                });
            }
            return Collects.findFirst(EnumSet.allOf(targetClass), predicate);
        }
        throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("{} not a enum class", Reflects.getFQNClassName(targetClass)));
    }

    public static <T extends Enum<T>> EnumSet<T> getEnumSet(Class<T> enumClass) {
        return EnumSet.allOf(enumClass);
    }

    public static <T extends Enum<T>> List<T> getEnumList(Class<T> enumClass) {
        return Collects.asList(getEnumSet(enumClass));
    }


    public static int getIndex(Enum e) {
        return e.ordinal();
    }

    public static int getCode(Enum e) {
        if (e instanceof CommonEnum) {
            return ((CommonEnum) e).getCode();
        }
        return getIndex(e);
    }

    public static String getName(Enum e) {
        if (e instanceof CommonEnum) {
            return ((CommonEnum) e).getName();
        }
        return e.name();
    }

    public static String getDisplayText(Enum e) {
        if (e instanceof CommonEnum) {
            return ((CommonEnum) e).getDisplayText();
        }
        return e.name();
    }

}
