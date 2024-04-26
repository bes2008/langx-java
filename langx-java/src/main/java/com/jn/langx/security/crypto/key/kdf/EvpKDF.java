package com.jn.langx.security.crypto.key.kdf;

import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.io.Charsets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EvpKDF implements KDF{
    @Override
    public DerivedKey generate(String passphrase, byte[] saltBytes, int keyBitSize, int ivBitSize, int iterations, String messageDigestAlgorithm) throws NoSuchAlgorithmException {
        int keyBytesLength= PKIs.getBytesLength(keyBitSize);
        int ivBytesLength=PKIs.getBytesLength(ivBitSize);
        byte[] key=new byte[keyBytesLength];
        byte[] iv=new byte[ivBytesLength];

        // 4个字符作为一个 word
        int keySizeInWord = keyBytesLength / 4;
        int ivSizeInWord = ivBytesLength / 4;
        int targetKeySizeInWorld = keySizeInWord + ivSizeInWord;
        // 由 key ,iv进行拼接的
        byte[] derivedBytes = new byte[targetKeySizeInWorld * 4];
        int numberOfDerivedWords = 0;
        byte[] block = null;
        byte[] passphraseBytes= passphrase.getBytes(Charsets.UTF_8);
        MessageDigest hasher = MessageDigest.getInstance(messageDigestAlgorithm);
        while (numberOfDerivedWords < targetKeySizeInWorld) {
            if (block != null) {
                hasher.update(block);
            }
            hasher.update(passphraseBytes);
            block = hasher.digest(saltBytes);
            hasher.reset();

            // Iterations
            for (int i = 1; i < iterations; i++) {
                block = hasher.digest(block);
                hasher.reset();
            }

            System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4,
                    Math.min(block.length, (targetKeySizeInWorld - numberOfDerivedWords) * 4));

            numberOfDerivedWords += block.length/4;
        }

        System.arraycopy(derivedBytes, 0, key, 0, keySizeInWord * 4);
        System.arraycopy(derivedBytes, keySizeInWord * 4, iv, 0, ivSizeInWord * 4);

        DerivedKey result= new DerivedKey();
        result.setSalt(saltBytes);
        result.setSecretKey(key);
        result.setIv(iv);
        result.setDerivedBytes(derivedBytes);
        return result;
    }
}
