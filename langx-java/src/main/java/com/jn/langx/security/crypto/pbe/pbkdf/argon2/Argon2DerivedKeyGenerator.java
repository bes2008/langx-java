package com.jn.langx.security.crypto.pbe.pbkdf.argon2;

import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.pbe.pbkdf.DerivedKeyGenerator;
import com.jn.langx.security.crypto.pbe.pbkdf.SimpleDerivedKey;

/**
 * @since 5.5.0
 */
public class Argon2DerivedKeyGenerator extends DerivedKeyGenerator {

    private Argon2Parameters parameters;

    public Argon2DerivedKeyGenerator() {
    }

    public void init(byte[] password, Argon2Parameters parameters) {
        this.password = password;
        this.parameters = parameters;
    }

    @Override
    public SimpleDerivedKey generateDerivedKey(int keyBitSize) {
        int keyBytesLength = Securitys.getBytesLength(keyBitSize);
        byte[] derivedKey = new byte[keyBytesLength];
        Argon2BytesGenerator delegate = new Argon2BytesGenerator();
        delegate.init(parameters);
        delegate.generateBytes(password, derivedKey);
        return new SimpleDerivedKey(derivedKey);
    }

    @Override
    public SimpleDerivedKey generateDerivedKeyWithIV(int keyBitSize, int ivBitSize) {
        return generateDerivedKey(keyBitSize);
    }

    @Override
    public SimpleDerivedKey generateDerivedKeyUseHMac(int keyBitSize) {
        return generateDerivedKey(keyBitSize);
    }
}
