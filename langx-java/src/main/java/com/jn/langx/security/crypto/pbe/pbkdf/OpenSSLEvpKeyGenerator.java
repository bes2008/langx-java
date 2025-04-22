package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.SecurityException;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.io.bytes.Bytes;

import java.security.MessageDigest;

public class OpenSSLEvpKeyGenerator extends DerivedKeyGenerator {
    private String digestAlgorithm;

    public OpenSSLEvpKeyGenerator() {

    }

    public void setDigestAlgorithm(String digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    @Override
    public SimpleDerivedKey generateDerivedKey(int keySize) {
        return generateDerivedKeyWithIV(keySize, keySize);
    }

    @Override
    public SimpleDerivedKey generateDerivedKeyWithIV(int keyBitSize, int ivBitSize) {
        try {
            int keyBytesLength = PKIs.getBytesLength(keyBitSize);
            int ivBytesLength = PKIs.getBytesLength(ivBitSize);

            int bytesLength = keyBytesLength + ivBytesLength;

            // 由 key ,iv进行拼接的
            MessageDigest hasher = MessageDigest.getInstance(digestAlgorithm);
            hasher.update(password);
            byte[] block = hasher.digest(salt);
            hasher.reset();


            while (block.length < bytesLength) {
                hasher.update(block);
                hasher.update(password);
                block = hasher.digest(salt);
                hasher.reset();
            }

            byte[] key = Bytes.subBytes(block, 0, keyBytesLength);
            byte[] iv = Bytes.subBytes(block, keyBytesLength, ivBytesLength);
            return new SimpleDerivedKey(key, iv);
        } catch (Throwable e) {
            throw new SecurityException(e);
        }
    }

    @Override
    public SimpleDerivedKey generateDerivedKeyUseHMac(int keySize) {
        return generateDerivedKey(keySize);
    }
}
