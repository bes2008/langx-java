package com.jn.langx.util.io;

public interface WritableComparable<T> extends Writable, Comparable<T> {
    @Override
    int compareTo(T o);
}
