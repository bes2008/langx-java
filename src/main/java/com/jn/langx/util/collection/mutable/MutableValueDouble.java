package com.jn.langx.util.collection.mutable;

/**
 * {@link MutableValue} implementation of type <code>double</code>.
 * When mutating instances of this object, the caller is responsible for ensuring
 * that any instance where <code>exists</code> is set to <code>false</code> must also
 * <code>value</code> set to <code>0.0D</code> for proper operation.
 */
public class MutableValueDouble extends MutableValue {
    public double value = 0.0D;

    @Override
    public Object toObject() {
        assert exists || 0.0D == value;
        return exists ? value : null;
    }

    @Override
    public void copy(MutableValue source) {
        MutableValueDouble s = (MutableValueDouble) source;
        value = s.value;
        exists = s.exists;
    }

    @Override
    public MutableValue duplicate() {
        MutableValueDouble v = new MutableValueDouble();
        v.value = this.value;
        v.exists = this.exists;
        return v;
    }

    @Override
    public boolean equalsSameType(Object other) {
        assert exists || 0.0D == value;
        MutableValueDouble b = (MutableValueDouble) other;
        return value == b.value && exists == b.exists;
    }

    @Override
    public int compareSameType(Object other) {
        assert exists || 0.0D == value;
        MutableValueDouble b = (MutableValueDouble) other;
        int c = Double.compare(value, b.value);
        if (c != 0) {
            return c;
        }
        if (exists == b.exists) {
            return 0;
        }
        return exists ? 1 : -1;
    }

    @Override
    public int hashCode() {
        assert exists || 0.0D == value;
        long x = Double.doubleToLongBits(value);
        return (int) x + (int) (x >>> 32);
    }
}