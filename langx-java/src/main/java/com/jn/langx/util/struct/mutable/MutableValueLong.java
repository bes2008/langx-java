package com.jn.langx.util.struct.mutable;

/**
 * {@link MutableValue} implementation of type <code>long</code>.
 * When mutating instances of this object, the caller is responsible for ensuring
 * that any instance where <code>exists</code> is set to <code>false</code> must also
 * <code>value</code> set to <code>0L</code> for proper operation.
 */
public class MutableValueLong extends MutableValue {
    public long value;

    @Override
    public Object toObject() {
        assert exists || 0L == value;
        return exists ? value : null;
    }

    @Override
    public void copy(MutableValue source) {
        MutableValueLong s = (MutableValueLong) source;
        exists = s.exists;
        value = s.value;
    }

    @Override
    public MutableValue duplicate() {
        MutableValueLong v = new MutableValueLong();
        v.value = this.value;
        v.exists = this.exists;
        return v;
    }

    @Override
    public boolean equalsSameType(Object other) {
        assert exists || 0L == value;
        MutableValueLong b = (MutableValueLong) other;
        return value == b.value && exists == b.exists;
    }

    @Override
    public int compareSameType(Object other) {
        assert exists || 0L == value;
        MutableValueLong b = (MutableValueLong) other;
        long bv = b.value;
        if (value < bv) {
            return -1;
        }
        if (value > bv) {
            return 1;
        }
        if (exists == b.exists) {
            return 0;
        }
        return exists ? 1 : -1;
    }


    @Override
    public int hashCode() {
        assert exists || 0L == value;
        return (int) value + (int) (value >> 32);
    }
}