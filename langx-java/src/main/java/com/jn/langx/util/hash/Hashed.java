package com.jn.langx.util.hash;

/**
 * The Hashed interface defines the basic operations for setting and getting a hash value.
 * This interface allows a class to have its hash value set and retrieved, which is useful in scenarios where hashing is needed.
 */
public interface Hashed {
    /**
     * Sets the hash value for this object.
     *
     * @param hash The hash value to be set.
     */
    void setHash(int hash);

    /**
     * Gets the hash value of this object.
     *
     * @return The current hash value of this object.
     */
    int getHash();
}
