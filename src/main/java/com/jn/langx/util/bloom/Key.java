package com.jn.langx.util.bloom;


import com.jn.langx.util.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * The general behavior of a key that must be stored in a filter.
 *
 * @see Filter The general behavior of a filter
 */
public class Key implements WritableComparable<Key> {
    /**
     * Byte value of key
     */
    byte[] bytes;

    /**
     * The weight associated to <i>this</i> key.
     * <p>
     * <b>Invariant</b>: if it is not specified, each instance of
     * <code>Key</code> will have a default weight of 1.0
     */
    double weight;

    /**
     * default constructor - use with readFields
     */
    public Key() {
    }

    /**
     * Constructor.
     * <p>
     * Builds a key with a default weight.
     *
     * @param value The byte value of <i>this</i> key.
     */
    public Key(byte[] value) {
        this(value, 1.0);
    }

    /**
     * Constructor.
     * <p>
     * Builds a key with a specified weight.
     *
     * @param value  The value of <i>this</i> key.
     * @param weight The weight associated to <i>this</i> key.
     */
    public Key(byte[] value, double weight) {
        set(value, weight);
    }

    /**
     * @param value
     * @param weight
     */
    public void set(byte[] value, double weight) {
        if (value == null) {
            throw new IllegalArgumentException("value can not be null");
        }
        this.bytes = value;
        this.weight = weight;
    }

    /**
     * @return byte[] The value of <i>this</i> key.
     */
    public byte[] getBytes() {
        return this.bytes;
    }

    /**
     * @return Returns the weight associated to <i>this</i> key.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Increments the weight of <i>this</i> key with a specified value.
     *
     * @param weight The increment.
     */
    public void incrementWeight(double weight) {
        this.weight += weight;
    }

    /**
     * Increments the weight of <i>this</i> key by one.
     */
    public void incrementWeight() {
        this.weight++;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Key)) {
            return false;
        }
        return this.compareTo((Key) o) == 0;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result ^= Byte.valueOf(bytes[i]).hashCode();
        }
        result ^= Double.valueOf(weight).hashCode();
        return result;
    }

    // Writable

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(bytes.length);
        out.write(bytes);
        out.writeDouble(weight);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.bytes = new byte[in.readInt()];
        in.readFully(this.bytes);
        weight = in.readDouble();
    }

    // Comparable
    @Override
    public int compareTo(Key other) {
        int result = this.bytes.length - other.getBytes().length;
        for (int i = 0; result == 0 && i < bytes.length; i++) {
            result = this.bytes[i] - other.bytes[i];
        }

        if (result == 0) {
            result = Double.valueOf(this.weight - other.weight).intValue();
        }
        return result;
    }
}