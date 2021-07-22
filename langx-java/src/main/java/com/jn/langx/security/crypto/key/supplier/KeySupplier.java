package com.jn.langx.security.crypto.key.supplier;

import java.security.Key;

public interface KeySupplier<I1, I2, I3, O extends Key> {
    O get(I1 i1, I2 i2, I3 i3);
}
