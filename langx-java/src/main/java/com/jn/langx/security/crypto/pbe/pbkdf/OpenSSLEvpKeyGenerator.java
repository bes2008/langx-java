package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.digest.MessageDigests;
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
        // 由 key ,iv进行拼接的
        MessageDigest hasher = MessageDigests.getDigest(digestAlgorithm);
        byte[] dk = new byte[bytesLength];
        int offset = 0;

        byte[] buffer = new byte[0];
        while (true) {
            if (buffer.length > 0) {
                hasher.update(buffer);
            }
            hasher.update(password);
            buffer = hasher.digest(salt);
            hasher.reset();

            int len = (offset + buffer.length) > bytesLength ? (bytesLength - offset) : buffer.length;
            System.arraycopy(buffer, 0, dk, offset, len);
            offset = offset + len;
            if (offset >= bytesLength) {
                break;
            }
        }

        return dk;
    }

    @Override
    public SimpleDerivedKey generateDerivedKeyUseHMac(int keyBitSize) {
        return generateDerivedKey(keyBitSize);
    }
}
