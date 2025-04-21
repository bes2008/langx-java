package com.jn.langx.security.crypto.pbe.pbkdf;

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
