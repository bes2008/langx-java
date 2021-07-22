package com.jn.langx.security.crypto.key.supplier.bytesbased;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Provider;

public class ByteBasedSecretKeySupplier implements BytesBasedKeySupplier<SecretKey> {
    @Override
    public SecretKey get(byte[] bytes, String algorithm, Provider provider) {
        SecretKey secretKey = new SecretKeySpec(bytes, algorithm);
        return secretKey;
    }
}
