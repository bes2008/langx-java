package com.jn.langx.util.hash;

public abstract class OnceCalculateHasher extends Hasher {
    private byte[] temp = new byte[16];
    private int nextPutIndex = 0;

    @Override
    protected void update(byte b) {
        if (nextPutIndex >= temp.length) {
            byte[] temp2 = new byte[temp.length * 2];
            System.arraycopy(temp, 0, temp2, 0, nextPutIndex);
            temp = temp2;
        }
        temp[nextPutIndex++] = b;
    }

    @Override
    public int get() {
        int h = hash(temp, nextPutIndex, seed);
        reset();
        return h;
    }

    @Override
    protected void reset() {
        temp = new byte[16];
        nextPutIndex = 0;
        seed = -1;
    }
}
