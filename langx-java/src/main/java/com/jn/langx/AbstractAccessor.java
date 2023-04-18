package com.jn.langx;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.*;

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
    public boolean isNull(K key) {
        return !has(key) || get(key) == null;
    }

    @Override
    public boolean isEmpty(K key) {
        return Objs.isEmpty(key);
    }

    @Override
    public Object get(K key, @NonNull Function<Object, Object> mapper) {
        return mapper.apply(get(key));
    }

    @Override
    public String getString(K key, Function<Object, String> mapper) {
        return mapper.apply(get(key));
    }

    @Override
    public Character getCharacter(K key, Function<Object, Character> mapper) {
        return mapper.apply(get(key));
    }

    @Override
    public Byte getByte(K key, Function<Object, Byte> mapper) {
        return mapper.apply(get(key));
    }

    @Override
    public Integer getInteger(K key, Function<Object, Integer> mapper) {
        return mapper.apply(get(key));
    }

    @Override
    public Short getShort(K key, Function<Object, Short> mapper) {
        return mapper.apply(get(key));
    }

    @Override
    public Double getDouble(K key, Function<Object, Double> mapper) {
        return mapper.apply(get(key));
    }

    @Override
    public Float getFloat(K key, Function<Object, Float> mapper) {
        return mapper.apply(get(key));
    }

    @Override
    public Long getLong(K key, Function<Object, Long> mapper) {
        return mapper.apply(get(key));
    }

    @Override
    public Boolean getBoolean(K key, Function<Object, Boolean> mapper) {
        return mapper.apply(get(key));
    }

    @Override
    public <E> E getAny(K... keys) {
        return getAny(Functions.<K, E>nullPredicate2(), keys);
    }

    @Override
    public <E> E getAny(Predicate2<K, E> predicate, K... keys) {
        E v = Pipeline.<K>of(keys)
                .firstMap(new Function2<Integer, K, E>() {
                    @Override
                    public E apply(Integer keyIndex, K key) {
                        return (E) get(key);
                    }
                }, predicate);
        return v;
    }
}
