package com.jn.langx.security.crypto.pbe.pbkdf;

/**
 * @since 5.5.0
 */
public class PBKDF2DerivedKeyGeneratorFactory implements DerivedKeyGeneratorFactory<PBKDF2DerivedKeyGenerator> {
    @Override
    public PBKDF2DerivedKeyGenerator get(PBKDFKeySpec keySpec) {
        PBKDF2DerivedKeyGenerator generator = new PBKDF2DerivedKeyGenerator();
        generator.init(new PasswordToPkcs5Utf8Converter().apply(keySpec.getPassword()), keySpec.getSalt(), keySpec.getIterationCount());
        generator.setHmacAlgorithm(keySpec.getHashAlgorithm());
        return generator;
    }
}
