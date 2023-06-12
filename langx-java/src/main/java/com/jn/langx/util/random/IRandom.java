package com.jn.langx.util.random;

public interface IRandom {
    void setSeed(long seed);
    int nextInt();

    int nextInt(int bound);

    long nextLong();

    boolean nextBoolean();

    float nextFloat();

    double nextDouble();

    void nextBytes(byte[] dest);
}
