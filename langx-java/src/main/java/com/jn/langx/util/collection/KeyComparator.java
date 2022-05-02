package com.jn.langx.util.collection;

import java.io.Serializable;

/**
 * A key comparator.
 *
 * @param <K> Key's type
 * @author Paul Sandoz
 */
public interface KeyComparator<K> extends Serializable {

    /**
     * Compare two keys for equality.
     *
     * @param x the first key
     * @param y the second key
     * @return true if the keys are equal.
     */
    boolean equals(K x, K y);

    /**
     * Get the hash code of a key.
     * @param k the key.
     * @return the hash code of the key.
     */
    int hash(K k);
}
