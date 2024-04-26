package com.jn.langx.security.crypto.key.pb;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.codec.hex.Hex;
import com.jn.langx.text.StringTemplates;

/**
 * 代表派生出来的key 封装
 */
public class DerivedKey {
    /**
     * 生成的 salt
     */
    @NonNull
    private byte[] salt;
    /**
     * 生成的指定长度的key，这个代表了实际的key
     */
    @NonNull
    private byte[] secretKey;
    /**
     * 生成的指定长度在 IV
     */
    @Nullable
    private byte[] iv;

    /**
     * 生成过程中产生的一个临时key
     */
    private byte[] derivedBytes;

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(byte[] secretKey) {
        this.secretKey = secretKey;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public byte[] getDerivedBytes() {
        return derivedBytes;
    }

    public void setDerivedBytes(byte[] derivedBytes) {
        this.derivedBytes = derivedBytes;
    }

    @Override
    public String toString() {
        return StringTemplates.formatWithPlaceholder( "salt: {}\nkey: {}\niv: {}", Hex.encodeHexString(salt), Hex.encodeHexString(secretKey), iv==null?"":Hex.encodeHexString(iv));
    }
}
