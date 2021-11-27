package com.jn.langx.java8.util.stream;

import com.jn.langx.util.io.IOs;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility class to help with the usage of the Java 8 Streams API in Gaffer.
 * @since 4.1.0
 */
public final class Streams {

    /**
     * Convert an {@link java.lang.Iterable} to a {@link java.util.stream.Stream}
     * The stream returned must be closed.
     *
     * @param iterable the input iterable
     * @param <T>      the type of object stored in the iterable
     * @return a stream containing the contents of the iterable
     */
    public static <T> Stream<T> toStream(final Iterable<T> iterable) {
        if (iterable instanceof StreamIterable) {
            return ((StreamIterable<T>) iterable).extractStream();
        }

        return StreamSupport.stream(iterable.spliterator(), false)
                .onClose(() -> IOs.close(iterable));
    }

    /**
     * Convert an array to a {@link java.util.stream.Stream}.
     *
     * @param array the input array
     * @param <T>   the type of object stored in the array
     * @return a stream containing the contents of the array
     */
    public static <T> Stream<T> toStream(final T[] array) {
        return StreamSupport.stream(Spliterators.spliterator(array, 0), false);
    }

    /**
     * Convert an {@link java.util.Iterator} to a {@link java.util.stream.Stream}
     * The stream returned must be closed.
     *
     * @param iterator the input iterator
     * @param <T>      the type of object stored in the iterator
     * @return a stream containing the contents of the iterator
     */
    public static <T> Stream<T> toStream(final Iterator<T> iterator) {
        if (iterator instanceof StreamIterator) {
            return ((StreamIterator<T>) iterator).getStream();
        }

        final Iterable<T> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false)
                .onClose(() -> IOs.close(iterator));
    }

    /**
     * Convert an {@link java.lang.Iterable} to a {@link java.util.stream.Stream}
     * The stream returned must be closed.
     *
     * @param iterable the input iterable
     * @param <T>      the type of object stored in the iterable
     * @return a stream containing the contents of the iterable
     */
    public static <T> Stream<T> toParallelStream(final Iterable<T> iterable) {
        if (iterable instanceof StreamIterable) {
            return ((StreamIterable<T>) iterable).extractStream().parallel();
        }

        return StreamSupport.stream(iterable.spliterator(), true)
                .onClose(() -> IOs.close(iterable));
    }

    /**
     * Convert an {@link java.util.Iterator} to a {@link java.util.stream.Stream}
     * The stream returned must be closed.
     *
     * @param iterator the input iterator
     * @param <T>      the type of object stored in the iterator
     * @return a stream containing the contents of the iterator
     */
    public static <T> Stream<T> toParallelStream(final Iterator<T> iterator) {
        if (iterator instanceof StreamIterator) {
            return ((StreamIterator<T>) iterator).getStream().parallel();
        }

        final Iterable<T> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), true)
                .onClose(() -> IOs.close(iterator));
    }

    /**
     * Convert an array to a {@link java.util.stream.Stream}.
     *
     * @param array the input array
     * @param <T>   the type of object stored in the array
     * @return a stream containing the contents of the array
     */
    public static <T> Stream<T> toParallelStream(final T[] array) {
        return StreamSupport.stream(Spliterators.spliterator(array, 0), true);
    }

    private Streams() {
        // Private constructor to prevent instantiation
    }
}
