package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.codec.hex.Hex;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;

import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * 代表派生出来的key
 *
 * 例子：
 * pbeAlgorithm：PBEWithMD5AndAES
 * cipherAlgorithm: AES
 * hashAlgorithm: MD5
 *
 *
 * @since 5.3.9
 */
public class DerivedPBEKey extends IvParameterSpec implements PBEKey, Cloneable {

    @NonNull
    private PBKDFKeySpec keySpec;

    @NonNull
    private String pbeAlgorithm;

    @Nullable
    private String cipherAlgorithm;

    /**
     * 生成的指定长度的key，这个代表了实际的key
     */
    @NonNull
    private byte[] key;

    @Nullable
    private byte[] iv;

    public DerivedPBEKey(String pbeAlgorithm, PBKDFKeySpec keySpec, byte[] key){
        this(pbeAlgorithm, null, keySpec, key, null);
    }


    public DerivedPBEKey(String pbeAlgorithm, PBKDFKeySpec keySpec, byte[] key, byte[] iv){
        this(pbeAlgorithm, null, keySpec, key, iv);
    }

    public DerivedPBEKey(String pbeAlgorithm, String cipherAlgorithm, PBKDFKeySpec keySpec, byte[] key, byte[] iv){
        super(Objs.useValueIfEmpty(iv, Emptys.EMPTY_BYTES));
        setIV(super.getIV());
        this.key=key;
        this.pbeAlgorithm=pbeAlgorithm;
        this.cipherAlgorithm=cipherAlgorithm;
        this.keySpec=keySpec;
    }

    @Override
    public String toString() {
        return StringTemplates.formatWithPlaceholder( "salt: {}\nkey: {}\niv: {}", Hex.encodeHexString(keySpec.getSalt()), Hex.encodeHexString(key), getIV()==null?"":Hex.encodeHexString(getIV()));
    }

    public void setIV(byte[] iv) {
        this.iv = iv;
    }

    @Override
    public byte[] getIV() {
        return this.iv;
    }

    @Override
    public byte[] getEncoded() {
        return this.key;
    }

    @Override
    public String getFormat() {
        return Base64.encodeBase64String(this.key);
    }



    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public char[] getPassword() {
        return this.keySpec.getPassword();
    }

    @Override
    public byte[] getSalt() {
        return this.keySpec.getSalt();
    }

    @Override
    public int getIterationCount() {
        return this.keySpec.getIterationCount();
    }

    @Override
    public String getAlgorithm() {
        return this.pbeAlgorithm;
    }

    public String getCipherAlgorithm(){
        return cipherAlgorithm;
    }
    public PBKDFKeySpec getKeySpec() {
        return keySpec;
    }

    public int getKeyBitSize(){
        return this.keySpec.getKeyLength();
    }

    public int getIVBitSize(){
        return this.keySpec.getIvBitSize();
    }

    public String getHashAlgorithm(){
        return this.keySpec.getHashAlgorithm();
    }
}
