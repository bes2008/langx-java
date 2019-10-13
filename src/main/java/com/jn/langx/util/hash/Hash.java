package com.jn.langx.util.hash;

public abstract class Hash {
    /**
     * Constant to denote invalid hash type.
     */
    public static final int INVALID_HASH = -1;
    /**
     * Constant to denote {@link JenkinsHash}.
     */
    public static final int JENKINS_HASH = 0;
    /**
     * Constant to denote {@link MurmurHash}.
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
    public static Hash getInstance(int type) {
        switch (type) {
            case JENKINS_HASH:
                return JenkinsHash.getInstance();
            case MURMUR_HASH:
                return MurmurHash.getInstance();
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
    public int hash(byte[] bytes) {
        return hash(bytes, bytes.length, -1);
    }

    /**
     * Calculate a hash using all bytes from the input argument,
     * and a provided seed value.
     *
     * @param bytes   input bytes
     * @param initval seed value
     * @return hash value
     */
    public int hash(byte[] bytes, int initval) {
        return hash(bytes, bytes.length, initval);
    }

    /**
     * Calculate a hash using bytes from 0 to <code>length</code>, and
     * the provided seed value
     *
     * @param bytes   input bytes
     * @param length  length of the valid bytes to consider
     * @param initval seed value
     * @return hash value
     */
    public abstract int hash(byte[] bytes, int length, int initval);
}
