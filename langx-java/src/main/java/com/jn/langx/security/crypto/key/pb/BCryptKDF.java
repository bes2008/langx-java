package com.jn.langx.security.crypto.key.pb;

import com.jn.langx.util.io.Charsets;

import java.security.NoSuchAlgorithmException;

public class BCryptKDF implements KDF{
    @Override
    public DerivedKey generate(String passphrase, byte[] saltBytes, int keyBitSize, int ivBitSize, int iterations, String hashAlgorithm) throws NoSuchAlgorithmException {
        String hashedPswd = BCrypt.hashpw(passphrase, new String(saltBytes, Charsets.UTF_8));
        byte[] key=hashedPswd.getBytes(Charsets.UTF_8);
        return new DerivedKey(saltBytes, key);
    }
}
