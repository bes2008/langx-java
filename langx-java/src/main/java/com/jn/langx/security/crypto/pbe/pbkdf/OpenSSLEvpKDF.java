package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.SecurityException;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.io.Charsets;

import java.security.MessageDigest;

/**
 * @since 5.3.9
 */
class OpenSSLEvpKDF implements PBKDF {

    @Override
    public DerivedPBEKey apply(String pbeAlgorithm, PBKDFKeySpec keySpec){
        try {
            int keyBytesLength = PKIs.getBytesLength(keySpec.getKeyLength());
            int ivBytesLength = PKIs.getBytesLength(keySpec.getIvBitSize());
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
            byte[] passphraseBytes = new String(keySpec.getPassword()).getBytes(Charsets.UTF_8);
            MessageDigest hasher = MessageDigest.getInstance(keySpec.getHashAlgorithm());
            while (numberOfDerivedWords < targetKeySizeInWorld) {
                if (block != null) {
                    hasher.update(block);
                }
                hasher.update(passphraseBytes);
                block = hasher.digest(keySpec.getSalt());
                hasher.reset();

                // Iterations
                for (int i = 1; i < keySpec.getIterationCount(); i++) {
                    block = hasher.digest(block);
                    hasher.reset();
                }

                System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4,
                        Math.min(block.length, (targetKeySizeInWorld - numberOfDerivedWords) * 4));

                numberOfDerivedWords += block.length / 4;
            }

            System.arraycopy(derivedBytes, 0, key, 0, keySizeInWord * 4);
            System.arraycopy(derivedBytes, keySizeInWord * 4, iv, 0, ivSizeInWord * 4);

            DerivedPBEKey derivedKey = new DerivedPBEKey(pbeAlgorithm, keySpec, key, iv);
            return derivedKey;
        }catch (Throwable e){
            throw new SecurityException(e);
        }
    }
}
