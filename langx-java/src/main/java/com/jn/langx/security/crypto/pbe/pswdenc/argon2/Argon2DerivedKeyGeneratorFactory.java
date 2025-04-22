package com.jn.langx.security.crypto.pbe.pswdenc.argon2;

import com.jn.langx.security.crypto.pbe.pbkdf.DerivedKeyGeneratorFactory;
import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFKeySpec;
import com.jn.langx.security.crypto.pbe.pbkdf.PasswordToPkcs5Utf8Converter;
import com.jn.langx.util.Preconditions;

/**
 * @since 5.5.0
 */
public class Argon2DerivedKeyGeneratorFactory implements DerivedKeyGeneratorFactory<Argon2DerivedKeyGenerator> {
    @Override
    public Argon2DerivedKeyGenerator get(PBKDFKeySpec keySpec) {
        Argon2KeySpec argon2KeySpec = (Argon2KeySpec) keySpec;
        Argon2DerivedKeyGenerator derivedKeyGenerator = new Argon2DerivedKeyGenerator();
        Preconditions.checkNotNull(keySpec.getPassword(), "password is null");
        derivedKeyGenerator.init(new PasswordToPkcs5Utf8Converter().apply(keySpec.getPassword()), argon2KeySpec.getParameters());
        return derivedKeyGenerator;
    }
}
