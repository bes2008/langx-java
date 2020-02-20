package com.jn.langx.util.struct.counter;

public interface Counter<E extends Number> {
    E increment();

    E increment(E delta);

    E decrement();

    E decrement(E delta);

    E get();

    void set(E value);
}
