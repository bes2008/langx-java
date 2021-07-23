package com.jn.langx.security.crypto.key.supplier.bytesbased;

import com.jn.langx.security.crypto.cipher.Asymmetrics;
import com.jn.langx.security.crypto.key.PKIs;

import java.security.Provider;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class BytesBasedPublicKeySupplier implements BytesBasedKeySupplier<PublicKey>{
    @Override
    public PublicKey get(byte[] bytes, String algorithm, Provider provider) {
        algorithm = Asymmetrics.getAlgorithmAfterWith(algorithm);
        PublicKey publicKey = PKIs.createPublicKey(algorithm, provider == null ? null : provider.getName(), new X509EncodedKeySpec(bytes));
        return publicKey;
    }
}
