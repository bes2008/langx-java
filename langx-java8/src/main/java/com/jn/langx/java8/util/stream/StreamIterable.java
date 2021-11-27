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

import com.jn.langx.util.collection.iter.CloseableIterable;
import com.jn.langx.util.collection.iter.CloseableIterator;
import com.jn.langx.util.io.IOs;

import java.util.stream.Stream;

/**
 * A {@link CloseableIterable} which uses
 * a {@link Stream} as the underlying data source.
 *
 * @param <T> the object type
 * @since 4.1.0
 */
public class StreamIterable<T> implements CloseableIterable<T> {
    private final StreamSupplier<T> streamSupplier;

    public StreamIterable(final StreamSupplier<T> streamSupplier) {
        this.streamSupplier = streamSupplier;
    }

    @Override
    public void close() {
        IOs.close(streamSupplier);
    }

    @Override
    public CloseableIterator<T> iterator() {
        return new StreamIterator<>(streamSupplier.get());
    }

    /**
     * Get a {@link Stream} from the {@link StreamSupplier}.
     * <p>
     * This enables the creation of multiple stream objects from the same base
     * data, without operating on the same stream multiple times.
     *
     * @return a new {@link Stream}
     */
    public Stream<T> extractStream() {
        return streamSupplier.get();
    }
}
