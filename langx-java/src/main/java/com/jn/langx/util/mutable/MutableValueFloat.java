package com.jn.langx.util.mutable;

/**
 * {@link MutableValue} implementation of type <code>float</code>.
 * When mutating instances of this object, the caller is responsible for ensuring
 * that any instance where <code>exists</code> is set to <code>false</code> must also
 * <code>value</code> set to <code>0.0F</code> for proper operation.
 */
public class MutableValueFloat extends MutableValue {
    public float value;

    @Override
    public Object toObject() {
        assert exists || 0.0F == value;
        return exists ? value : null;
    }

    @Override
    public void copy(MutableValue source) {
        MutableValueFloat s = (MutableValueFloat) source;
        value = s.value;
        exists = s.exists;
    }

    @Override
    public MutableValue duplicate() {
        MutableValueFloat v = new MutableValueFloat();
        v.value = this.value;
        v.exists = this.exists;
        return v;
    }

    @Override
    public boolean equalsSameType(Object other) {
        assert exists || 0.0F == value;
        MutableValueFloat b = (MutableValueFloat) other;
        return value == b.value && exists == b.exists;
    }

    @Override
    public int compareSameType(Object other) {
        assert exists || 0.0F == value;
        MutableValueFloat b = (MutableValueFloat) other;
        int c = Float.compare(value, b.value);
        if (c != 0) return c;
        if (exists == b.exists) return 0;
        return exists ? 1 : -1;
    }

    @Override
    public int hashCode() {
        assert exists || 0.0F == value;
        return Float.floatToIntBits(value);
    }
}