package com.jn.langx.util.hash;

/**
 * 有3种用法：
 * 1）一次性调用hash(byte[],,,,) 方法
 * 2）调用setSeed() , 多次调用 update(byte[]), 调用 get()
 * 3）多次调用 hash(byte[], length, seed)
 */
public abstract class Hasher {
    /**
     * Constant to denote invalid hash type.
     */
    public static final int INVALID_HASH = -1;
    /**
     * Constant to denote {@link JenkinsHasher}.
     */
    public static final int JENKINS_HASH = 0;
    /**
     * Constant to denote {@link Murmur2Hasher}.
     */
    public static final int MURMUR_HASH = 1;

    /**
     * This utility method converts String representation of hash function name
     * to a symbolic constant. Currently two function types are supported,
     * "jenkins" and "murmur".
     *
     * @param name hash function name
     * @return one of the predefined constants
     */
    public static int parseHashType(String name) {
        if ("jenkins".equalsIgnoreCase(name)) {
            return JENKINS_HASH;
        } else if ("murmur".equalsIgnoreCase(name)) {
            return MURMUR_HASH;
        } else {
            return INVALID_HASH;
        }
    }


    /**
     * Get a singleton instance of hash function of a given type.
     *
     * @param type predefined hash type
     * @return hash function instance, or null if type is invalid
     */
    public static Hasher getInstance(int type) {
        switch (type) {
            case JENKINS_HASH:
                return JenkinsHasher.getInstance();
            case MURMUR_HASH:
                return Murmur2Hasher.getInstance();
            default:
                return null;
        }
    }

    /**
     * Calculate a hash using all bytes from the input argument, and
     * a seed of -1.
     *
     * @param bytes input bytes
     * @return hash value
     */
    public long hash(byte[] bytes) {
        return hash(bytes, bytes.length, 0);
    }

    /**
     * Calculate a hash using all bytes from the input argument,
     * and a provided seed value.
     *
     * @param bytes input bytes
     * @param seed  seed value
     * @return hash value
     */
    public long hash(byte[] bytes, long seed) {
        return hash(bytes, bytes.length, seed);
    }

    /**
     * Calculate a hash using bytes from 0 to <code>length</code>, and
     * the provided seed value
     *
     * @param bytes  input bytes
     * @param length length of the valid bytes to consider
     * @param seed   seed value
     * @return hash value
     */
    public long hash(byte[] bytes, int length, long seed) {
        this.setSeed(seed);
        this.update(bytes, 0, length);
        return this.get();
    }

    protected long seed;

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void update(byte[] bytes, int off, int len) {
        for (int i = off; i < off + len; i++) {
            update(bytes[i]);
        }
    }

    protected abstract void update(byte b);

    protected void reset() {
        this.seed = -1;
    }

    public abstract long get();
}
