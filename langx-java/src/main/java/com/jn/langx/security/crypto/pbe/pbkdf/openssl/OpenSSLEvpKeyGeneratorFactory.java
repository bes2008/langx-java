package com.jn.langx.security.crypto.pbe.pbkdf.openssl;

import com.jn.langx.security.crypto.pbe.pbkdf.DerivedKeyGeneratorFactory;
import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFKeySpec;
import com.jn.langx.security.crypto.pbe.pswdconverter.PasswordToPkcs12Converter;

public class OpenSSLEvpKeyGeneratorFactory implements DerivedKeyGeneratorFactory<OpenSSLEvpKeyGenerator> {
    @Override
    public OpenSSLEvpKeyGenerator get(PBKDFKeySpec keySpec) {
        OpenSSLEvpKeyGenerator keyGenerator = new OpenSSLEvpKeyGenerator();
        keyGenerator.init(new PasswordToPkcs12Converter().apply(keySpec.getPassword()),
                keySpec.getSalt(),
                keySpec.getIterationCount());
        keyGenerator.setDigestAlgorithm(keySpec.getHashAlgorithm());
        return keyGenerator;
    }
}
