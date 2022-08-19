package com.jn.langx.util.concurrent.longaddr;


/**
 * Abstract interface for objects that can concurrently add longs.
 *
 * @since 4.7.4
 */
interface LongAddable {
    void increment();

    void add(long x);

    long sum();
}
