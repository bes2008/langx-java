package com.jn.langx.security.pbe.cipher.kdf;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.codec.hex.Hex;
import com.jn.langx.text.StringTemplates;

import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * 代表派生出来的key
 *
 *
 * 例子：
 * pbeAlgorithm：PBEWithMD5AndAES
 * cipherAlgorithm: AES
 * hashAlgorithm: MD5
 */
public class DerivedKey extends IvParameterSpec implements PBEKey, Cloneable {
    @Nullable
    private String pbeAlgorithm;

    private String cipherAlgorithm;

    private String hashAlgorithm;

    private char[] password;

    private int iterationCount;


    /**
     * 生成的 salt
     */
    @NonNull
    private byte[] salt;
    /**
     * 生成的指定长度的key，这个代表了实际的key
     */
    @NonNull
    private byte[] key;

    /**
     * 生成过程中产生的一个临时key
     */
    private byte[] derivedBytes;

    public DerivedKey(byte[] salt, byte[] key){
        this(salt,key,null,null);
    }

    public DerivedKey(byte[] salt, byte[] key,  byte[] derivedBytes){
        this(salt,key,null,derivedBytes);
    }
    public DerivedKey(byte[] salt, byte[] key, byte[] iv, byte[] derivedBytes){
        super(iv);
        this.salt=salt;
        this.key=key;
        this.derivedBytes=derivedBytes;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getDerivedBytes() {
        return derivedBytes;
    }

    public void setDerivedBytes(byte[] derivedBytes) {
        this.derivedBytes = derivedBytes;
    }

    @Override
    public String toString() {
        return StringTemplates.formatWithPlaceholder( "salt: {}\nkey: {}\niv: {}", Hex.encodeHexString(salt), Hex.encodeHexString(key), getIV()==null?"":Hex.encodeHexString(getIV()));
    }

    @Override
    public byte[] getEncoded() {
        return this.key;
    }

    @Override
    public String getFormat() {
        return Base64.encodeBase64String(this.key);
    }

    public void setAlgorithm(String algorithm) {
        this.pbeAlgorithm = algorithm;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    @Override
    public String getAlgorithm() {
        return this.pbeAlgorithm;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public char[] getPassword() {
        return this.password;
    }

    @Override
    public int getIterationCount() {
        return this.iterationCount;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getCipherAlgorithm() {
        return cipherAlgorithm;
    }

    public void setCipherAlgorithm(String cipherAlgorithm) {
        this.cipherAlgorithm = cipherAlgorithm;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }
}
