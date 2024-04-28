package com.jn.langx.security.pbe.pbkdf;

import com.jn.langx.security.crypto.JCAEStandardName;

import javax.crypto.spec.PBEKeySpec;

public class PBKDFKeySpec extends PBEKeySpec {
    private int ivBitSize;
    private String hashAlgorithm;

    /**
     * 这里不是 pbe algorithm，是对称加密算法的名字
     */
    private String cipherAlgorithm;

    public PBKDFKeySpec(char[] password, byte[] salt,  int keyBitSize){
        this(password, salt, keyBitSize, 16*8, 1);
    }

    public PBKDFKeySpec(char[] password, byte[] salt,  int keyBitSize, int iterationCount){
        this(password, salt, keyBitSize, 16*8, iterationCount);
    }

    public PBKDFKeySpec(char[] password, byte[] salt, int keySize, int ivBitSize, int iterationCount){
        this(password, salt,  keySize, ivBitSize, iterationCount, JCAEStandardName.SHA_256.getName());
    }

    public PBKDFKeySpec(char[] password, byte[] salt,  int keySize, int ivBitSize,int iterationCount, String hashAlgorithm){
        super(password, salt, iterationCount, keySize);
        setHashAlgorithm(hashAlgorithm);
        setIvBitSize(ivBitSize);
    }



    public int getIvBitSize() {
        return ivBitSize;
    }

    public void setIvBitSize(int ivBitSize) {
        this.ivBitSize = ivBitSize;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }
}
