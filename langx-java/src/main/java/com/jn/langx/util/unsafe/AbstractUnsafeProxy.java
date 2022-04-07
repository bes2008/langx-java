package com.jn.langx.util.unsafe;


public abstract class AbstractUnsafeProxy implements UnsafeProxy {

    private static UnsafeProxy theUnsafe;

    public int getInt(Object o, int offset) {
        return getInt(o, (long) offset);
    }

    public void putInt(Object o, int offset, int x) {
        putInt(o, (long) offset, x);
    }

    public Object getObject(Object o, int offset) {
        return getObject(o, (long) offset);
    }

    public void putObject(Object o, int offset, Object x) {
        putObject(o, (long) offset, x);
    }


    public boolean getBoolean(Object o, int offset) {
        return getBoolean(o, (long) offset);
    }


    public void putBoolean(Object o, int offset, boolean x) {
        putBoolean(o, (long) offset, x);
    }

    public byte getByte(Object o, int offset) {
        return getByte(o, (long) offset);
    }

    public void putByte(Object o, int offset, byte x) {
        putByte(o, (long) offset, x);
    }

    public short getShort(Object o, int offset) {
        return getShort(o, (long) offset);
    }

    public void putShort(Object o, int offset, short x) {
        putShort(o, (long) offset, x);
    }

    public char getChar(Object o, int offset) {
        return getChar(o, (long) offset);
    }

    public void putChar(Object o, int offset, char x) {
        putChar(o, (long) offset, x);
    }


    public long getLong(Object o, int offset) {
        return getLong(o, (long) offset);
    }


    public void putLong(Object o, int offset, long x) {
        putLong(o, (long) offset, x);
    }


    public float getFloat(Object o, int offset) {
        return getFloat(o, (long) offset);
    }

    public void putFloat(Object o, int offset, float x) {
        putFloat(o, (long) offset, x);
    }

    public double getDouble(Object o, int offset) {
        return getDouble(o, (long) offset);
    }

    public void putDouble(Object o, int offset, double x) {
        putDouble(o, (long) offset, x);
    }


    public int getAndAddInt(Object o, long offset, int delta) {
        int v;
        do {
            v = getIntVolatile(o, offset);
        } while (!compareAndSwapInt(o, offset, v, v + delta));
        return v;
    }

    public long getAndAddLong(Object o, long offset, long delta) {
        long v;
        do {
            v = getLongVolatile(o, offset);
        } while (!compareAndSwapLong(o, offset, v, v + delta));
        return v;
    }

    public int getAndSetInt(Object o, long offset, int newValue) {
        int v;
        do {
            v = getIntVolatile(o, offset);
        } while (!compareAndSwapInt(o, offset, v, newValue));
        return v;
    }

    public long getAndSetLong(Object o, long offset, long newValue) {
        long v;
        do {
            v = getLongVolatile(o, offset);
        } while (!compareAndSwapLong(o, offset, v, newValue));
        return v;
    }

    public Object getAndSetObject(Object o, long offset, Object newValue) {
        Object v;
        do {
            v = getObjectVolatile(o, offset);
        } while (!compareAndSwapObject(o, offset, v, newValue));
        return v;
    }

}
