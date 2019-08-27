package com.jn.langx.util.collection.mutable;

/**
 * Base class for all mutable values.
 */
public abstract class MutableValue implements Comparable<MutableValue> {
    public boolean exists = true;

    public abstract void copy(MutableValue source);

    public abstract MutableValue duplicate();

    public abstract boolean equalsSameType(Object other);

    public abstract int compareSameType(Object other);

    public abstract Object toObject();

    public boolean exists() {
        return exists;
    }

    @Override
    public int compareTo(MutableValue other) {
        Class<? extends MutableValue> c1 = this.getClass();
        Class<? extends MutableValue> c2 = other.getClass();
        if (c1 != c2) {
            int c = c1.hashCode() - c2.hashCode();
            if (c == 0) {
                c = c1.getCanonicalName().compareTo(c2.getCanonicalName());
            }
            return c;
        }
        return compareSameType(other);
    }

    @Override
    public boolean equals(Object other) {
        return (getClass() == other.getClass()) && this.equalsSameType(other);
    }

    @Override
    public abstract int hashCode();

    @Override
    public String toString() {
        return exists() ? toObject().toString() : "(null)";
    }
}
