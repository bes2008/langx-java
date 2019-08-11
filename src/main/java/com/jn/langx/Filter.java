package com.jn.langx;

public interface Filter<E> {
    boolean accept(E e);
}
