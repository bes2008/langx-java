package com.jn.langx.security.gm.crypto.sm2;

import com.jn.langx.security.gm.GmJceProvider;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedKeySupplier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.Provider;
import java.security.PublicKey;

public class BytesBasedSM2PublicKeySupplier implements BytesBasedKeySupplier<PublicKey> {
    private static final Logger logger = LoggerFactory.getLogger(BytesBasedSM2PublicKeySupplier.class);

    @Override
    public PublicKey get(byte[] bytes, String algorithm, Provider provider) {
        if ("SM2".equals(algorithm) && provider.getName().equals(BouncyCastleProvider.PROVIDER_NAME) || provider.getName().equals(GmJceProvider.NAME)) {
            SubjectPublicKeyInfo keyInfo = SubjectPublicKeyInfo.getInstance(bytes);
            PublicKey publicKey = null;
            try {
                publicKey = new KeyFactorySpi.EC().generatePublic(keyInfo);
                return publicKey;
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return null;
    }
}
