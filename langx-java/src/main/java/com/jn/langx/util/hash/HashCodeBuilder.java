package com.jn.langx.util.hash;

import com.jn.langx.Builder;

public class HashCodeBuilder implements Builder<Integer> {
    private int hash;

    public HashCodeBuilder() {
        this(0);
    }

    public HashCodeBuilder(int hash) {
        if (hash < 0) {
            hash = 0;
        }
        this.hash = hash;
    }

    public HashCodeBuilder with(Object object) {
        compute(object == null ? 0 : object.hashCode());
        return this;
    }

    private void compute(int hash) {
        this.hash = this.hash * 31 + hash;
    }

    public Integer build() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HashCodeBuilder that = (HashCodeBuilder) o;

        return hash == that.hash;
    }

    @Override
    public int hashCode() {
        return hash;
    }


    public static int generate(boolean value) {
        return value ? 1231 : 1237;
    }

    public static int generate(long value) {
        return (int) (value ^ value >> 32);
    }

    public static int generate(double value) {
        return generate(Double.doubleToLongBits(value));
    }

    public static int generate(float value) {
        return Float.floatToIntBits(value);
    }

    public static int generate(byte[] bytes) {
        int hashcode = 0;

        for (byte aByte : bytes) {
            hashcode <<= 1;
            hashcode ^= aByte;
        }

        return hashcode;
    }

    public static int generate(Object[] array, boolean deep) {
        int hashcode = 0;

        for (Object o : array) {
            if (deep && o instanceof Object[]) {
                hashcode ^= generate(((Object[]) o), true);
            } else {
                hashcode ^= o.hashCode();
            }
        }

        return hashcode;
    }

    public static int generate(Object[] array) {
        return generate(array, false);
    }

    public static int generate(Object obj) {
        return obj != null ? obj.hashCode() : 0;
    }
}
