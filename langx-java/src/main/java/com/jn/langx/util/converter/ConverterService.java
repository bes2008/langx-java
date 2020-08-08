package com.jn.langx.util.converter;


import com.jn.langx.Converter;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.exception.ValueConvertException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConverterService {
    public static final ConverterService DEFAULT;
    /**
     * key: target class
     * value:
     */
    private static final Map<Class, Converter> TARGET_BUILTIN = new HashMap<Class, Converter>();

    private static final Map<Class, ConcurrentHashMap<Class, Converter>> TARGET_SOURCE_BUILTIN = new HashMap<Class, ConcurrentHashMap<Class, Converter>>();

    static {
        TARGET_BUILTIN.put(Byte.class, ByteConverter.INSTANCE);
        TARGET_BUILTIN.put(byte.class, ByteConverter.INSTANCE);
        TARGET_BUILTIN.put(Short.class, ShortConverter.INSTANCE);
        TARGET_BUILTIN.put(short.class, ShortConverter.INSTANCE);
        TARGET_BUILTIN.put(char.class, CharacterConverter.INSTANCE);
        TARGET_BUILTIN.put(Character.class, CharacterConverter.INSTANCE);
        TARGET_BUILTIN.put(Integer.class, IntegerConverter.INSTANCE);
        TARGET_BUILTIN.put(int.class, IntegerConverter.INSTANCE);
        TARGET_BUILTIN.put(Long.class, LongConverter.INSTANCE);
        TARGET_BUILTIN.put(long.class, LongConverter.INSTANCE);
        TARGET_BUILTIN.put(Float.class, FloatConverter.INSTANCE);
        TARGET_BUILTIN.put(float.class, FloatConverter.INSTANCE);
        TARGET_BUILTIN.put(Double.class, DoubleConverter.INSTANCE);
        TARGET_BUILTIN.put(double.class, DoubleConverter.INSTANCE);
        TARGET_BUILTIN.put(boolean.class, BooleanConverter.INSTANCE);
        TARGET_BUILTIN.put(Boolean.class, BooleanConverter.INSTANCE);

        ConcurrentHashMap<Class, Converter> toDateConverterMap = new ConcurrentHashMap<Class, Converter>();
        toDateConverterMap.put(String.class, StringToDateConverter.INSTANCE);
        toDateConverterMap.put(Long.class, LongToDateConverter.INSTANCE);
        TARGET_SOURCE_BUILTIN.put(Date.class, toDateConverterMap);

        DEFAULT = new ConverterService();
    }

    /**
     * 没有 source 类型，只有 target 类型
     */
    private final Map<Class, Converter> target_registry = new ConcurrentHashMap<Class, Converter>(TARGET_BUILTIN);

    /**
     * 有 source 类型，也有 target 类型
     * key： target class
     * sub key: source class
     */
    private ConcurrentHashMap<Class, ConcurrentHashMap<Class, Converter>> target_source_registry = new ConcurrentHashMap<Class, ConcurrentHashMap<Class, Converter>>(TARGET_SOURCE_BUILTIN);

    public void register(@NonNull Class targetClass, @Nullable Class sourceClass, @NonNull Converter converter) {
        if (sourceClass == null) {
            register(targetClass, converter);
            return;
        }
        Preconditions.checkNotNull(targetClass);
        Preconditions.checkNotNull(converter);
        ConcurrentHashMap<Class, Converter> map = Maps.putIfAbsent(target_source_registry, targetClass, new Function<Class, ConcurrentHashMap<Class, Converter>>() {
            @Override
            public ConcurrentHashMap<Class, Converter> apply(Class input) {
                return new ConcurrentHashMap<Class, Converter>();
            }
        });
        map.put(sourceClass, converter);
    }

    public void register(@NonNull Class targetClass, @NonNull Converter converter) {
        Preconditions.checkNotNull(targetClass);
        Preconditions.checkNotNull(converter);
        target_registry.put(targetClass, converter);
    }

    public <T> T convert(@Nullable Object obj, @NonNull Class<T> targetClass) {
        Converter converter = findConverter(obj, targetClass);

        if (converter == null) {
            if (obj == null) {
                return null;
            }
            throw new ValueConvertException(StringTemplates.formatWithPlaceholder("Can't cast {} to {}", obj, targetClass));
        }
        return (T) converter.apply(obj);
    }

    /**
     * 根据 source 是否 为 null 自行决定从哪个registry
     *
     * @param source
     * @param targetClass
     * @param <S>
     * @param <T>
     * @return
     */
    public <S, T> Converter<S, T> findConverter(@Nullable S source, @NonNull Class<T> targetClass) {
        Preconditions.checkNotNull(targetClass);
        if (source == null) {
            return target_registry.get(targetClass);
        }

        Converter converter = findConverter(source.getClass(), targetClass);
        if (converter == null) {
            converter = target_registry.get(targetClass);
        }
        if (converter == null) {
            if (source.getClass() == targetClass) {
                return NoopConverter.INSTANCE;
            }
        }
        return converter;
    }

    /**
     * 从 target-source-registry 中找
     *
     * @param sourceClass
     * @param targetClass
     * @param <S>
     * @param <T>
     * @return
     */
    public <S, T> Converter<S, T> findConverter(@NonNull Class<S> sourceClass, @NonNull final Class<T> targetClass) {
        Preconditions.checkNotNull(sourceClass);
        Preconditions.checkNotNull(targetClass);

        ConcurrentHashMap<Class, Converter> mapping = target_source_registry.get(targetClass);
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
