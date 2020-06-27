package com.jn.langx.util.converter;


import com.jn.langx.Converter;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.exception.ValueConvertException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConverterService {
    /**
     * key: target class
     * value:
     */
    private static final Map<Class, Converter> BUILTIN = new HashMap<Class, Converter>();

    static {
        BUILTIN.put(Byte.class, ByteConverter.INSTANCE);
        BUILTIN.put(byte.class, ByteConverter.INSTANCE);
        BUILTIN.put(Short.class, ShortConverter.INSTANCE);
        BUILTIN.put(short.class, ShortConverter.INSTANCE);
        BUILTIN.put(Integer.class, IntegerConverter.INSTANCE);
        BUILTIN.put(int.class, IntegerConverter.INSTANCE);
        BUILTIN.put(Long.class, LongConverter.INSTANCE);
        BUILTIN.put(long.class, LongConverter.INSTANCE);
        BUILTIN.put(Float.class, FloatConverter.INSTANCE);
        BUILTIN.put(float.class, FloatConverter.INSTANCE);
        BUILTIN.put(Double.class, DoubleConverter.INSTANCE);
        BUILTIN.put(double.class, DoubleConverter.INSTANCE);
        BUILTIN.put(boolean.class, BooleanConverter.INSTANCE);
        BUILTIN.put(Boolean.class, BooleanConverter.INSTANCE);
    }


    private ConcurrentHashMap<Class, ConcurrentHashMap<Class, Converter>> registry0 = new ConcurrentHashMap<Class, ConcurrentHashMap<Class, Converter>>();
    private final Map<Class, Converter> registry1 = new ConcurrentHashMap<Class, Converter>(BUILTIN);

    public static final ConverterService DEFAULT = new ConverterService();

    public void register(@NonNull Class targetClass, @Nullable Class sourceClass, @NonNull Converter converter) {
        if (sourceClass == null) {
            register(targetClass, converter);
            return;
        }
        Preconditions.checkNotNull(targetClass);
        Preconditions.checkNotNull(converter);
        ConcurrentHashMap<Class, Converter> map = registry0.get(targetClass);
        if (map == null) {
            map = new ConcurrentHashMap<Class, Converter>();
            registry0.put(targetClass, map);
        }
        map.put(sourceClass, converter);
    }

    public void register(@NonNull Class targetClass, @NonNull Converter converter) {
        Preconditions.checkNotNull(targetClass);
        Preconditions.checkNotNull(converter);
        registry1.put(targetClass, converter);
    }

    public <T> T convert(@Nullable Object obj, @NonNull Class<T> targetClass) {
        Converter converter = findConverter(obj == null ? null : obj.getClass(), targetClass);
        if (converter == null) {
            throw new ValueConvertException(StringTemplates.formatWithPlaceholder("Can't cast {} to {}", obj, targetClass));
        }
        return (T) converter.apply(obj);
    }

    public <S, T> Converter<S, T> findConverter(@Nullable S source, @NonNull Class<T> targetClass) {
        Preconditions.checkNotNull(targetClass);
        if (source == null) {
            return registry1.get(targetClass);
        }

        Converter converter = findConverter(source.getClass(), targetClass);
        if (converter == null) {
            converter = findConverter(null, targetClass);
        }
        return converter;
    }


    public <S, T> Converter<S, T> findConverter(@NonNull Class<S> sourceClass, @NonNull final Class<T> targetClass) {
        Preconditions.checkNotNull(sourceClass);
        Preconditions.checkNotNull(targetClass);

        ConcurrentHashMap<Class, Converter> mapping = registry0.get(targetClass);
        if (mapping != null) {
            Converter converter = mapping.get(sourceClass);
            if (converter != null) {
                return converter;
            }
            Map.Entry<? extends Class, ? extends Converter> entry = Collects.findFirst(mapping, new Predicate2<Class, Converter>() {
                @Override
                public boolean test(Class sourceClass, Converter converter) {
                    return converter.isConvertible(sourceClass, targetClass);
                }
            });
            if (entry != null) {
                return entry.getValue();
            }
        }
        return null;
    }

}
