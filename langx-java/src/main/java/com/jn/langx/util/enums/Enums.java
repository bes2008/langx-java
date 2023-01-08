package com.jn.langx.util.enums;

import com.jn.langx.Named;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.function.Supplier0;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.spi.CommonServiceProvider;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Enums {
    private Enums() {

    }

    private final static Map<Class, EnumGetter> GETTER_REGISTRY;

    static {
        final Map<Class, EnumGetter> r = Maps.newLinkedHashMap();
        Pipeline.<EnumGetter>of(new CommonServiceProvider<EnumGetter>().get(EnumGetter.class)).forEach(new Consumer<EnumGetter>() {
            @Override
            public void accept(final EnumGetter selector) {
                List<Class> targetEnumClass = selector.applyTo();
                Pipeline.of(targetEnumClass).forEach(new Consumer<Class>() {
                    @Override
                    public void accept(Class aClass) {
                        r.put(aClass, selector);
                    }
                });

            }
        });
        GETTER_REGISTRY = r;
    }

    private static EnumGetter selectGetter(final Class tClass) {
        EnumGetter selector = GETTER_REGISTRY.get(tClass);
        if (selector == null && Reflects.isSubClass(CommonEnum.class, tClass)) {
            selector = GETTER_REGISTRY.get(CommonEnum.class);
        }

        if (selector == null) {
            Pipeline.of(GETTER_REGISTRY.keySet()).findFirst(new Predicate<Class>() {
                @Override
                public boolean test(Class aClass) {
                    if (aClass == Enum.class) {
                        return false;
                    }
                    if (Reflects.isSubClassOrEquals(aClass, tClass)) {
                        return true;
                    }
                    return false;
                }
            });

        }
        if (selector == null) {
            selector = GETTER_REGISTRY.get(Enum.class);
        }
        return selector;
    }

    /**
     * 基于 ordinal
     *
     * @param value
     * @param targetClass
     * @param <T>
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
     */
    public static <T extends Enum<T>> T ofCode(@NonNull final Class<T> tClass, final int code) {
        Preconditions.checkNotNull(tClass);
        EnumGetter getter = selectGetter(tClass);
        T t = (T) getter.getByCode(tClass, code);
        return t;
    }

    /**
     * 基于name
     *
     * @param tClass
     * @param name
     * @param <T>
     */
    public static <T extends Enum<T>> T ofName(@NonNull final Class<T> tClass, final String name) {
        Preconditions.checkNotNull(tClass);
        EnumGetter getter = selectGetter(tClass);
        T t = (T) getter.getByName(tClass, name);
        return t;
    }

    public static <T extends Enum<T>> T ofToString(@NonNull final Class<T> tClass, final String value) {
        Preconditions.checkNotNull(tClass);
        EnumGetter getter = selectGetter(tClass);
        T t = (T) getter.getByToString(tClass, value);
        return t;
    }

    public static <T extends Enum<T>> T ofDisplayText(@NonNull final Class<T> tClass, final String displayText) {
        Preconditions.checkNotNull(tClass);
        EnumGetter getter = selectGetter(tClass);
        T t = (T) getter.getByDisplayText(tClass, displayText);
        return t;
    }

    public static <T extends Enum<T>> T inferEnum(Class<T> targetClass, String text) {
        if (targetClass.isEnum() && Reflects.isSubClassOrEquals(Enum.class, targetClass)) {
            T v = Enums.<T>ofDisplayText(targetClass, text);
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
                        return Objs.deepEquals(fieldValue, expectedValue);
                    }
                });
            }
            return Collects.findFirst(EnumSet.allOf(targetClass), predicate);
        }
        throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("{} not a enum class", Reflects.getFQNClassName(targetClass)));
    }

    public static <T extends Enum<T>> Set<T> getEnumSet(Class<T> enumClass) {
        return EnumSet.allOf(enumClass);
    }

    public static <T extends Enum<T>> List<T> getEnumList(Class<T> enumClass) {
        return Collects.asList(getEnumSet(enumClass));
    }


    public static int getIndex(Enum e) {
        return e.ordinal();
    }

    public static int getCode(Enum e) {
        EnumGetter getter = selectGetter(e.getClass());
        int code = getter.getCode(e);
        return code;
    }

    public static String getName(Enum e) {
        if (e instanceof Named) {
            return ((Named) e).getName();
        }
        EnumGetter getter = selectGetter(e.getClass());
        String name = getter.getName(e);
        return name;
    }

    public static String getDisplayText(Enum e) {
        EnumGetter getter = selectGetter(e.getClass());
        String name = getter.getDisplayText(e);
        return name;
    }

}
