package com.jn.langx.util.collection;

import com.jn.langx.util.Objs;

import java.util.Arrays;

/**
 * @since 5.1.3
 */
public class ArrayKey {
    private final Object[] keys;
    private final int hashCode;

    /**
     * Constructs an instance of {@code MultipartKey} to hold the specified objects.
     *
     * @param keys the set of objects that make up the key.  Each key may be null.
     */
    public ArrayKey(final Object... keys) {
        this.keys = keys;
        this.hashCode = Objs.hashCode(keys);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ArrayKey other = (ArrayKey) obj;
        return Arrays.deepEquals(keys, other.keys);
    }


}
