package com.jn.langx.security.crypto.pbe.pbkdf.opensslevp;

import com.jn.langx.security.crypto.pbe.pbkdf.DerivedKeyGeneratorFactory;
import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFKeySpec;
import com.jn.langx.security.crypto.pbe.pbkdf.PasswordToPkcs5Utf8Converter;

/**
 * @since 5.5.0
 */
public class OpenSSLEvpKeyGeneratorFactory implements DerivedKeyGeneratorFactory<OpenSSLEvpKeyGenerator> {
    @Override
    public OpenSSLEvpKeyGenerator get(PBKDFKeySpec keySpec) {
        OpenSSLEvpKeyGenerator keyGenerator = new OpenSSLEvpKeyGenerator();
        keyGenerator.init(new PasswordToPkcs5Utf8Converter().apply(keySpec.getPassword()),
                keySpec.getSalt(),
                keySpec.getIterationCount());
        keyGenerator.setDigestAlgorithm(keySpec.getHashAlgorithm());
        return keyGenerator;
    }
}
