package com.jn.langx.security.crypto.key.spec.der;

import com.jn.langx.security.crypto.key.spec.PrivateKeySpecParser;

import java.security.spec.PKCS8EncodedKeySpec;

public class Pkcs8PrivateKeySpecParser implements PrivateKeySpecParser<PKCS8EncodedKeySpec> {
    public static final Pkcs8PrivateKeySpecParser INSTANCE = new Pkcs8PrivateKeySpecParser();

    @Override
    public PKCS8EncodedKeySpec parse(byte[] derEncodedBytes) {
        return new PKCS8EncodedKeySpec(derEncodedBytes);
    }
}
