package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.SecurityException;
import com.jn.langx.security.crypto.key.PKIs;

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
            byte[] key = new byte[keyBytesLength];
            byte[] iv = new byte[ivBytesLength];

            // 4个字符作为一个 word
            int keySizeInWord = keyBytesLength / 4;
            int ivSizeInWord = ivBytesLength / 4;
            int targetKeySizeInWorld = keySizeInWord + ivSizeInWord;
            // 由 key ,iv进行拼接的
            byte[] derivedBytes = new byte[targetKeySizeInWorld * 4];
            int numberOfDerivedWords = 0;
            byte[] block = null;

            MessageDigest hasher = MessageDigest.getInstance(digestAlgorithm);
            while (numberOfDerivedWords < targetKeySizeInWorld) {
                if (block != null) {
                    hasher.update(block);
                }
                hasher.update(password);
                block = hasher.digest(salt);
                hasher.reset();

                // Iterations
                for (int i = 1; i < iterationCount; i++) {
                    block = hasher.digest(block);
                    hasher.reset();
                }

                System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4,
                        Math.min(block.length, (targetKeySizeInWorld - numberOfDerivedWords) * 4));

                numberOfDerivedWords += block.length / 4;
            }

            System.arraycopy(derivedBytes, 0, key, 0, keySizeInWord * 4);
            System.arraycopy(derivedBytes, keySizeInWord * 4, iv, 0, ivSizeInWord * 4);

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
