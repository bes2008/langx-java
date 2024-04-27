package com.jn.langx.security.pbe.cipher.kdf;

import com.jn.langx.security.crypto.JCAEStandardName;

public class PBKDFAlgorithmParameterSpec {
    private String pswd;
    private byte[] salt;


    private int saltBitSize=8*8;
    private int keyBitSize=32*8;
    private int ivBitSize=16*8;
    private int iterations=1;
    private String hashAlgorithm= JCAEStandardName.SHA_256.getName();

    public PBKDFAlgorithmParameterSpec(){

    }
    public PBKDFAlgorithmParameterSpec(String pswd, byte[] salt, int keyBitSize, int ivBitSize, int iterations, String hashAlgorithm){
        this(pswd, salt.length * 8, keyBitSize, ivBitSize, iterations, hashAlgorithm);
        setSalt(salt);
    }
    public PBKDFAlgorithmParameterSpec(String pswd, int saltBitSize, int keyBitSize, int ivBitSize, int iterations, String hashAlgorithm){
        this.pswd=pswd;
        this.saltBitSize=saltBitSize;
        this.keyBitSize=keyBitSize;
        this.ivBitSize=ivBitSize;
        this.iterations=iterations;
        this.hashAlgorithm=hashAlgorithm;
    }


    public String getPswd() {
        return pswd;
    }

    public void setPswd(String password) {
        this.pswd = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public int getKeyBitSize() {
        return keyBitSize;
    }

    public void setKeyBitSize(int keyBitSize) {
        this.keyBitSize = keyBitSize;
    }

    public int getIvBitSize() {
        return ivBitSize;
    }

    public void setIvBitSize(int ivBitSize) {
        this.ivBitSize = ivBitSize;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public void setSaltBitSize(int saltBitSize) {
        this.saltBitSize = saltBitSize;
    }

    public int getSaltBitSize() {
        return saltBitSize;
    }
}
