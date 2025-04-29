package com.jn.langx.security.crypto.pbe.pbkdf;

/**
 * @since 5.5.0
 */
public class SimpleDerivedKey {
    private byte[] derivedKey;
    private byte[] iv;

    public SimpleDerivedKey(byte[] derivedKey, byte[] iv) {
        this.derivedKey = derivedKey;
        this.iv = iv;
    }

    public SimpleDerivedKey(byte[] derivedKey) {
        this.derivedKey = derivedKey;
    }

    public byte[] getDerivedKey() {
        return derivedKey;
    }

    public byte[] getIv() {
        return iv;
    }
}
