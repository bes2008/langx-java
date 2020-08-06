package com.jn.langx.util.valuegetter;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.function.Function;

public interface ValueGetter2<K> extends ValueGetter<K, Object> {
    @Override
    Object get(K key);

    boolean has(K key);

    <X, V> V get(K key, @NonNull Function<X, V> mapper);

    String getString(K key);

    String getString(K key, String defaultValue);

    <X> String getString(K key, @NonNull Function<X, String> mapper);

    Character getCharacter(K key);

    Character getCharacter(K key, Character defaultValue);

    <X> Character getCharacter(K key, @NonNull Function<X, Character> mapper);

    Byte getByte(K key);

    Byte getByte(K key, Byte defaultValue);

    <X> Byte getByte(K key, @NonNull Function<X, Byte> mapper);

    Short getShort(K key);

    Short getShort(K key, Short defaultValue);

    <X> Short getShort(K key, @NonNull Function<X, Short> mapper);

    Integer getInteger(K key);

    Integer getInteger(K key, Integer defaultValue);

    <X> Integer getInteger(K key, @NonNull Function<X, Integer> mapper);

    Double getDouble(K key);

    Double getDouble(K key, Double defaultValue);

    <X> Double getDouble(K key, @NonNull Function<X, Double> mapper);

    Float getFloat(K key);

    Float getFloat(K key, Float defaultValue);

    <X> Float getFloat(K key, @NonNull Function<X, Float> mapper);

    Long getLong(K key);

    Long getLong(K key, Long defaultValue);

    <X> Long getLong(K key, @NonNull Function<X, Long> mapper);

    Boolean getBoolean(K key);

    Boolean getBoolean(K key, Boolean defaultValue);

    <X> Boolean getBoolean(K key, @NonNull Function<X, Boolean> mapper);

}
