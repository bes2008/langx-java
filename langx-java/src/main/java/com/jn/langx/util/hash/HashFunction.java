package com.jn.langx.util.hash;

import com.jn.langx.util.bloom.Filter;
import com.jn.langx.util.bloom.Key;

/**
 * Implements a hash object that returns a certain number of hashed values.
 *
 * @see Key The general behavior of a key being stored in a filter
 * @see Filter The general behavior of a filter
 */
public final class HashFunction {
    /**
     * The number of hashed values.
     */
    private int nbHash;

    /**
     * The maximum highest returned value.
     */
    private int maxValue;

    /**
     * Hashing algorithm to use.
     */
    private Hasher hashFunction;

    /**
     * Constructor.
     * <p>
     * Builds a hash function that must obey to a given maximum number of returned values and a highest value.
     *
     * @param maxValue The maximum highest returned value.
     * @param nbHash   The number of resulting hashed values.
     * @param hasherName type of the hashing function (see {@link Hasher}).
     */
    public HashFunction(int maxValue, int nbHash, String hasherName) {
        if (maxValue <= 0) {
            throw new IllegalArgumentException("maxValue must be > 0");
        }

        if (nbHash <= 0) {
            throw new IllegalArgumentException("nbHash must be > 0");
        }

        this.maxValue = maxValue;
        this.nbHash = nbHash;
        this.hashFunction = Hasher.getInstance(hasherName);
        if (this.hashFunction == null)
            throw new IllegalArgumentException("hashType must be known");
    }

    /**
     * Clears <i>this</i> hash function. A NOOP
     */
    public void clear() {
    }

    /**
     * Hashes a specified key into several integers.
     *
     * @param k The specified key.
     * @return The array of hashed values.
     */
    public int[] hash(Key k) {
        byte[] b = k.getBytes();
        if (b == null) {
            throw new NullPointerException("buffer reference is null");
        }
        if (b.length == 0) {
            throw new IllegalArgumentException("key length must be > 0");
        }
        int[] result = new int[nbHash];
        long h = 0;
        for (int i = 0; i < nbHash; i++) {
            h = hashFunction.hash(b, h);
            result[i] = Math.abs(Long.valueOf(h).intValue() % maxValue);
        }
        return result;
    }
}