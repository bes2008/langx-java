package com.jn.langx.util.random;

import com.jn.langx.util.Preconditions;

import java.util.Random;

public class RandomProxy<R extends Random> implements IRandom {
    private R random;

    public RandomProxy(R r) {
        this.random = r;
    }

    @Override
    public void setSeed(long seed) {
        random.setSeed(seed);
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    @Override
    public long nextLong() {
        return random.nextLong();
    }

    @Override
    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    @Override
    public float nextFloat() {
        return random.nextFloat();
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }

    @Override
    public void nextBytes(byte[] dest) {
        random.nextBytes(dest);
    }

    public static final <R extends Random> RandomProxy<R> of(R r){
        Preconditions.checkNotNull(r);
        return new RandomProxy<R>(r);
    }
}
