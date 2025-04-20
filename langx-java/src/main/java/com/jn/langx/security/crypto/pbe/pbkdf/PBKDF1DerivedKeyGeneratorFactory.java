package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.crypto.pbe.pswdconverter.PasswordToPkcs5Utf8Converter;

public class PBKDF1DerivedKeyGeneratorFactory implements DerivedKeyGeneratorFactory<PBKDF1DerivedKeyGenerator> {
    @Override
    public PBKDF1DerivedKeyGenerator get(PBKDFKeySpec keySpec) {
        PBKDF1DerivedKeyGenerator generator = new PBKDF1DerivedKeyGenerator();
        generator.init(new PasswordToPkcs5Utf8Converter().apply(keySpec.getPassword()), keySpec.getSalt(), keySpec.getIterationCount());
        generator.setDigestAlgorithm(keySpec.getHashAlgorithm());
        return generator;
    }
}
