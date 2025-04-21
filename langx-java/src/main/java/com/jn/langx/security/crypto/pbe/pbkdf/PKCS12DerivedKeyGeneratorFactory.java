package com.jn.langx.security.crypto.pbe.pbkdf;

public class PKCS12DerivedKeyGeneratorFactory implements DerivedKeyGeneratorFactory<PKCS12DerivedKeyGenerator> {
    @Override
    public PKCS12DerivedKeyGenerator get(PBKDFKeySpec keySpec) {
        PKCS12DerivedKeyGenerator generator = new PKCS12DerivedKeyGenerator(keySpec.getHashAlgorithm());
        generator.init(new PasswordToPkcs12Converter().apply(keySpec.getPassword()), keySpec.getSalt(), keySpec.getIterationCount());
        return generator;
    }
}
