package com.jn.langx.security.crypto.key.supplier.bytesbased;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.security.crypto.key.supplier.KeySupplier;

import java.security.Key;
import java.security.Provider;

public interface BytesBasedKeySupplier<O extends Key> extends KeySupplier<byte[], String, Provider, O> {
    @Override
    O get(@NonNull byte[] bytes, @NonNull String algorithm, @Nullable Provider provider);
}
