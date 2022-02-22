package com.jn.langx.util.valuegetter;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate2;

public interface ValueGetter2<K> extends ValueGetter<K, Object>{
    @Override
    Object get(K key);

    /**
     * 指定key不存在，或者存在key但值为null，这两种情况都返回true
     * @param key the key
     * @return whether the value is null for the specified key
     */
    boolean isNull(K key);

    /**
     * key对应的值，可以被Objs.isEmpty 判断为true
     * @param key the key
     * @return whether the value is empty for the specified key
     */
    boolean isEmpty(K key);

    boolean has(K key);

    Object get(K key, @NonNull Function<Object, Object> mapper);

    String getString(K key);

    String getString(K key, String defaultValue);

    String getString(K key, @NonNull Function<Object, String> mapper);

    Character getCharacter(K key);

    Character getCharacter(K key, Character defaultValue);

    Character getCharacter(K key, @NonNull Function<Object, Character> mapper);

    Byte getByte(K key);

    Byte getByte(K key, Byte defaultValue);

    Byte getByte(K key, @NonNull Function<Object, Byte> mapper);

    Short getShort(K key);

    Short getShort(K key, Short defaultValue);

    Short getShort(K key, @NonNull Function<Object, Short> mapper);

    Integer getInteger(K key);

    Integer getInteger(K key, Integer defaultValue);

    Integer getInteger(K key, @NonNull Function<Object, Integer> mapper);

    Double getDouble(K key);

    Double getDouble(K key, Double defaultValue);

    Double getDouble(K key, @NonNull Function<Object, Double> mapper);

    Float getFloat(K key);

    Float getFloat(K key, Float defaultValue);

    Float getFloat(K key, @NonNull Function<Object, Float> mapper);

    Long getLong(K key);

    Long getLong(K key, Long defaultValue);

    Long getLong(K key, @NonNull Function<Object, Long> mapper);

    Boolean getBoolean(K key);

    Boolean getBoolean(K key, Boolean defaultValue);

    Boolean getBoolean(K key, @NonNull Function<Object, Boolean> mapper);

    <E> E getAny(K... keys);
    <E> E getAny(Predicate2<K,E> predicate, K... keys);

}
