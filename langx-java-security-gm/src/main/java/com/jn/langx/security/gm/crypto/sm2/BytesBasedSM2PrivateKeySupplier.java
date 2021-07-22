package com.jn.langx.security.gm.crypto.sm2;

import com.jn.langx.security.gm.GmJceProvider;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedKeySupplier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.Provider;

public class BytesBasedSM2PrivateKeySupplier implements BytesBasedKeySupplier<PrivateKey> {
    private static final Logger logger = LoggerFactory.getLogger(BytesBasedSM2PrivateKeySupplier.class);

    @Override
    public PrivateKey get(byte[] bytes, String algorithm, Provider provider) {
        if ("SM2".equals(algorithm) && provider.getName().equals(BouncyCastleProvider.PROVIDER_NAME) || provider.getName().equals(GmJceProvider.NAME)) {
            PrivateKeyInfo keyInfo = PrivateKeyInfo.getInstance(bytes);
            PrivateKey privateKey = null;
            try {
                privateKey = new KeyFactorySpi.EC().generatePrivate(keyInfo);
                return privateKey;
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return null;
    }
}
