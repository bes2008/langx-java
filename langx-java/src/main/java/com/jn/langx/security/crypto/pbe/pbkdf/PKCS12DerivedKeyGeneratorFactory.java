package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.crypto.pbe.pswdconverter.PasswordToPkcs5Utf8Converter;

public class PKCS12DerivedKeyGeneratorFactory implements DerivedKeyGeneratorFactory<PKCS12DerivedKeyGenerator> {
    @Override
    public PKCS12DerivedKeyGenerator get(PBKDFKeySpec keySpec) {
        PKCS12DerivedKeyGenerator generator = new PKCS12DerivedKeyGenerator(keySpec.getHashAlgorithm());
        generator.init(new PasswordToPkcs5Utf8Converter().apply(keySpec.getPassword()), keySpec.getSalt(), keySpec.getIterationCount());
        return generator;
    }
}
