package com.jn.langx.util.random;

import com.jn.langx.Delegatable;
import com.jn.langx.util.Preconditions;

/**
 * @since 4.4.7
 */
public class CommonBytesRandom implements BytesRandom, Delegatable<IRandom> {
    private IRandom delegate;
    private int multiplier = 1;

    @Override
    public IRandom getDelegate() {
        return delegate;
    }

    @Override
    public void setDelegate(IRandom delegate) {
        this.delegate = delegate;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        Preconditions.checkArgument(multiplier >= 1, "multiplier >= 1, actual: {}", multiplier);
        this.multiplier = multiplier;
    }

    @Override
    public byte[] get(Integer size) {
        byte[] dest = new byte[size * multiplier];
        get(dest);
        return dest;
    }

    @Override
    public void get(byte[] dest) {
        Preconditions.checkNotNullArgument(dest, "dest");
        delegate.nextBytes(dest);
    }
}
