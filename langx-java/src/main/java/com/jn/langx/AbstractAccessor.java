package com.jn.langx;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.function.Function;

public abstract class AbstractAccessor<K, T> implements Accessor<K, T> {
    private T t;

    @Override
    public T getTarget() {
        return t;
    }

    @Override
    public void setTarget(@NonNull T target) {
        Preconditions.checkNotNull(target);
        this.t = target;
    }

    @Override
    public <X, V> V get(K key, @NonNull Function<X, V> mapper) {
        return mapper.apply((X) get(key));
    }

    @Override
    public <X> String getString(K key, Function<X, String> mapper) {
        return mapper.apply((X) get(key));
    }

    @Override
    public <X> Character getCharacter(K key, Function<X, Character> mapper) {
        return mapper.apply((X) get(key));
    }

    @Override
    public <X> Byte getByte(K key, Function<X, Byte> mapper) {
        return mapper.apply((X) get(key));
    }

    @Override
    public <X> Integer getInteger(K key, Function<X, Integer> mapper) {
        return mapper.apply((X) get(key));
    }

    @Override
    public <X> Short getShort(K key, Function<X, Short> mapper) {
        return mapper.apply((X) get(key));
    }

    @Override
    public <X> Double getDouble(K key, Function<X, Double> mapper) {
        return mapper.apply((X) get(key));
    }

    @Override
    public <X> Float getFloat(K key, Function<X, Float> mapper) {
        return mapper.apply((X) get(key));
    }

    @Override
    public <X> Long getLong(K key, Function<X, Long> mapper) {
        return mapper.apply((X) get(key));
    }

    @Override
    public <X> Boolean getBoolean(K key, Function<X, Boolean> mapper) {
        return mapper.apply((X) get(key));
    }


}
