package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.util.io.bytes.Bytes;

import java.security.MessageDigest;

/**
 * 参考链接：https://docs.openssl.org/master/man3/EVP_BytesToKey/
 */
public class OpenSSLEvpKeyGenerator extends DerivedKeyGenerator {
    private String digestAlgorithm;

    public OpenSSLEvpKeyGenerator() {

    }

    public void setDigestAlgorithm(String digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    public void init(byte[] password, byte[] salt, int iterationCount) {
        super.init(password, salt, iterationCount);
        if (iterationCount < 1) {
            this.iterationCount = 1;
        }
        // 要么是null,要么只有8个字节
        if (this.salt != null && this.salt.length < 8) {
            this.salt = Bytes.subBytes(this.salt, 0, 8);
        }
    }

    @Override
    public SimpleDerivedKey generateDerivedKey(int keyBitSize) {
        int keyBytesLength = Securitys.getBytesLength(keyBitSize);
        byte[] dk = generateBytes(keyBytesLength);
        return new SimpleDerivedKey(Bytes.subBytes(dk, 0, keyBytesLength));
    }

    @Override
    public SimpleDerivedKey generateDerivedKeyWithIV(int keyBitSize, int ivBitSize) {
        int keyBytesLength = Securitys.getBytesLength(keyBitSize);
        int ivBytesLength = Securitys.getBytesLength(ivBitSize);

        int bytesLength = keyBytesLength + ivBytesLength;
        byte[] dk = generateBytes(bytesLength);
        byte[] key = Bytes.subBytes(dk, 0, keyBytesLength);
        byte[] iv = Bytes.subBytes(dk, keyBytesLength, ivBytesLength);
        return new SimpleDerivedKey(key, iv);
    }

    private byte[] generateBytes(int bytesLength) {
        // key 长度至少 112 bit
        if (bytesLength * 8 < 112) {
            throw new IllegalArgumentException("the key size must be greater than or equals 112");
        }
        // 由 key ,iv进行拼接的
        MessageDigest hasher = MessageDigests.getDigest(digestAlgorithm);
        byte[] dk = new byte[bytesLength];
        int offset = 0;

        byte[] buffer = null;
        while (offset < bytesLength) {
            if (buffer != null) {
                hasher.update(buffer);
            }
            hasher.update(password);
            if (salt != null) {
                hasher.update(salt);
            }
            buffer = hasher.digest();

            for (int i = 1; i < iterationCount; i++) {
                buffer = hasher.digest(buffer);
            }

            int len = (offset + buffer.length) > bytesLength ? (bytesLength - offset) : buffer.length;
            System.arraycopy(buffer, 0, dk, offset, len);
            offset = offset + len;
        }

        return dk;
    }

    @Override
    public SimpleDerivedKey generateDerivedKeyUseHMac(int keyBitSize) {
        return generateDerivedKey(keyBitSize);
    }
}
