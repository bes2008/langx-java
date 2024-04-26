package com.jn.langx.security.crypto.cipher.pb;

import com.jn.langx.security.SecurityException;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.key.pb.DerivedKey;
import com.jn.langx.security.crypto.key.pb.KDF;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;

/**
 * Password Based Cipher
 */
public abstract class AbstractPBDKFCipher implements PBKDFCipher {
    /*******************************************************
     * KDF 相关属性算法、属性
     *******************************************************/
    private KDF kdf;
    private byte[] saltBytes;

    private int saltBitLength;
    private String password;
    private int keyBitSize;
    private int ivBitSize;
    private String hashAlgorithm;
    private int iterations;

    /**
     *
     * @param password
     * @param saltBitLength
     * @param keyBitSize
     * @param ivBitSize
     * @param hashAlgorithm
     * @param iterations
     */
    public AbstractPBDKFCipher(String password, int saltBitLength, int keyBitSize, int ivBitSize, String hashAlgorithm, int iterations){

        this.password = password;
        this.saltBitLength = saltBitLength;
        this.keyBitSize = keyBitSize;
        this.ivBitSize = ivBitSize;
        this.hashAlgorithm = hashAlgorithm;
        this.iterations = iterations;

    }

    public void setKdf(KDF kdf) {
        this.kdf = kdf;
    }

    public byte[] getSalt() {
        return saltBytes;
    }

    public void setSalt(byte[] saltBytes) {
        this.saltBytes = saltBytes;
    }

    @Override
    public byte[] encrypt(byte[] message) {
        if( Objs.isEmpty(this.saltBytes)){
            this.saltBytes = Securitys.randomBytes(this.saltBitLength);
        }
        try {
            DerivedKey derivedKey = kdf.generate(password, this.saltBytes, this.keyBitSize, this.ivBitSize, this.iterations, this.hashAlgorithm);
            return enc(message, derivedKey);
        }catch (Throwable e){
            throw new SecurityException(e);
        }
    }

    protected abstract byte[] enc(byte[] message, DerivedKey derivedKey);
    protected abstract byte[] dec(byte[] message, DerivedKey derivedKey);
    @Override
    public byte[] decrypt(byte[] encryptedText) {
        if(this.saltBitLength==0){
            this.saltBytes= Emptys.EMPTY_BYTES;
        }else {
            Preconditions.checkNotNull(this.saltBytes);
        }
        try {
            DerivedKey derivedKey = kdf.generate(password, this.saltBytes, this.keyBitSize, this.ivBitSize, this.iterations, this.hashAlgorithm);
            return dec(encryptedText, derivedKey);
        }catch (Throwable e){
            throw new SecurityException(e);
        }
    }
}
