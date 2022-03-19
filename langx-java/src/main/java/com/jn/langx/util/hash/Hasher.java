package com.jn.langx.util.hash;

import com.jn.langx.Factory;
import com.jn.langx.Named;

public interface Hasher extends Factory<Long, Hasher>, Named {
    /**
     * Calculate a hash using all bytes from the input argument, and
     * a seed of 0.
     *
     * @param bytes input bytes
     * @return hash value
     */
    public long hash(byte[] bytes);
    /**
     * 一次性计算 hash
     * <p>
     * Calculate a hash using all bytes from the input argument,
     * and a provided seed value.
     *
     * @param bytes input bytes
     * @param seed  seed value
     * @return hash value
     */
    public long hash(byte[] bytes, long seed);

    /**
     * 一次性计算 hash
     * <p>
     * Calculate a hash using bytes from 0 to <code>length</code>, and
     * the provided seed value
     *
     * @param bytes  input bytes
     * @param length length of the valid bytes to consider
     * @param seed   seed value
     * @return hash value
     */
    public long hash(byte[] bytes, int length, long seed);

    @Override
    Hasher get(Long input);

    @Override
    String getName();
}
