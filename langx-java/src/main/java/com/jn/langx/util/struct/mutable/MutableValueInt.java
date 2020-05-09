package com.jn.langx.util.struct.mutable;

/**
 * {@link MutableValue} implementation of type <code>int</code>.
 * When mutating instances of this object, the caller is responsible for ensuring
 * that any instance where <code>exists</code> is set to <code>false</code> must also
 * <code>value</code> set to <code>0</code> for proper operation.
 */
public class MutableValueInt extends MutableValue {
    public int value;

    @Override
    public Object toObject() {
        assert exists || 0 == value;
        return exists ? value : null;
    }

    @Override
    public void copy(MutableValue source) {
        MutableValueInt s = (MutableValueInt) source;
        value = s.value;
        exists = s.exists;
    }

    @Override
    public MutableValue duplicate() {
        MutableValueInt v = new MutableValueInt();
        v.value = this.value;
        v.exists = this.exists;
        return v;
    }

    @Override
    public boolean equalsSameType(Object other) {
        assert exists || 0 == value;
        MutableValueInt b = (MutableValueInt) other;
        return value == b.value && exists == b.exists;
    }

    @Override
    public int compareSameType(Object other) {
        assert exists || 0 == value;
        MutableValueInt b = (MutableValueInt) other;
        int ai = value;
        int bi = b.value;
        if (ai < bi) {
            return -1;
        } else if (ai > bi) {
            return 1;
        }

        if (exists == b.exists) {
            return 0;
        }
        return exists ? 1 : -1;
    }


    @Override
    public int hashCode() {
        assert exists || 0 == value;
        return (value >> 8) + (value >> 16);
    }
}