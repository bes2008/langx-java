package com.jn.langx.java8.util.stream;


import java.io.Closeable;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @since 4.1.0
 */
public interface StreamSupplier<T> extends Supplier<Stream<T>>, Closeable {
}
