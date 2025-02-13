/*
 * Copyright 2016-2020 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.langx.util.collection.iter;

import java.io.Closeable;

/**
 * A {@code CloseableIterable} is an {@link Iterable} which must provide an implementation
 * of the {@link Closeable#close()} method.
 *
 * This interface is used to manage resources properly when iterating over elements, ensuring that resources are released after use.
 * For example, when iterating over files or network connections, using a {@code CloseableIterable} ensures that these resources are properly closed after the iteration is complete.
 *
 * @since 4.1.0
 * @param <T> the type of items in the iterable.
 */
public interface CloseableIterable<T> extends Iterable<T>, Closeable {
    /**
     * Closes this iterable, releasing any resources it may hold.
     *
     * Implementations of this method should ensure that all resources are properly released.
     * For example, when dealing with file operations, this method should ensure that all open file streams are closed.
     *
     */
    @Override
    void close();

    /**
     * Returns a {@link CloseableIterator} for iterating over the elements of this iterable.
     *
     * This method allows the creation of an iterator that also implements the {@link Closeable} interface,
     * ensuring that resources are properly released after iteration.
     *
     * @return a {@link CloseableIterator} for iterating over the elements of this iterable.
     */
    @Override
    CloseableIterator<T> iterator();
}
