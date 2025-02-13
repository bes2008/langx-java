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
import java.util.Iterator;

/**
 * A {@code CloseableIterator} is an {@link Iterator} which must provide an implementation
 * of the {@link Closeable#close()} method.
 *
 * <p>When implementing this interface, one must ensure that the {@link #close()} method
 * is called to release any resources held by the iterator, such as closing database connections
 * or file streams. This interface is particularly useful in scenarios where the iterator
 * may hold resources that need to be explicitly released to avoid leaks.</p>
 *
 * @param <T> the type of items in the iterator.
 * @since 4.1.0
 */
public interface CloseableIterator<T> extends Iterator<T>, Closeable {
    /**
     * Closes the iterator and releases any resources it may hold.
     *
     * <p>After calling this method, the iterator should no longer be used, as its internal
     * state may have been altered or resources it depended on may have been released.</p>
     */
    @Override
    void close();
}
