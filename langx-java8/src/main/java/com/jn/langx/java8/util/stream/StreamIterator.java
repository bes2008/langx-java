/*
 * Copyright 2017-2020 Crown Copyright
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

package com.jn.langx.java8.util.stream;

import com.jn.langx.util.collection.iter.CloseableIterator;
import com.jn.langx.util.collection.iter.EmptyCloseableIterator;
import com.jn.langx.util.io.IOs;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * A {@link CloseableIterator} to compliment
 * the {@link StreamIterable}.
 *
 * @since 4.1.0
 * @param <T> the object type
 */
public class StreamIterator<T> implements CloseableIterator<T> {
    private final Stream<T> stream;
    private final Iterator<T> iterator;

    public StreamIterator() {
        this(null);
    }

    public StreamIterator(final Stream<T> stream) {
        if (null == stream) {
            this.stream = Stream.empty();
            this.iterator = new EmptyCloseableIterator<>();
        } else {
            this.stream = stream;
            this.iterator = stream.iterator();
        }
    }

    @Override
    public void close() {
        IOs.close(iterator);
        IOs.close(stream);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    public Stream<T> getStream() {
        return stream;
    }
}
