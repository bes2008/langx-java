package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;

import java.security.MessageDigest;

/**
 * @see <a href="https://www.rfc-editor.org/rfc/rfc8018#section-6.2">PBKDF1</a>
 * @see <a href="org.bouncycastle.crypto.generators.PKCS5S1ParametersGenerator">PKCS5S1ParametersGenerator</a>
 * @since 5.5.0
 */
public class PBKDF1DerivedKeyGenerator extends DerivedKeyGenerator {
    private String digestAlgorithm;

    public PBKDF1DerivedKeyGenerator() {

    }

    public void setDigestAlgorithm(String digestAlgorithm) {
        Preconditions.checkNotEmpty(digestAlgorithm);
        // PBKDF1 only support MD2, MD5, SHA-1
        String[] supportedAlgorithms = new String[]{JCAEStandardName.MD2.getName(), JCAEStandardName.MD5.getName(), JCAEStandardName.SHA_1.getName()};
        if (!Collects.contains(supportedAlgorithms, Strings.upperCase(this.digestAlgorithm))) {
            throw new IllegalArgumentException("PBKDF1 only support MD2, MD5, SHA-1");
        }
        this.digestAlgorithm = digestAlgorithm;
    }

    @Override
    public void init(byte[] password, byte[] salt, int iterationCount) {
        super.init(password, salt, iterationCount);
        if (iterationCount < 1) {
            this.iterationCount = 1;
        }
        // salt 只能是 8 bytes
        Preconditions.checkArgument(this.salt.length == 8, "the salt length must be 8 bytes");
    }

    @Override
    public SimpleDerivedKey generateDerivedKey(int keyBitSize) {
        int dKeyBytesLength = Securitys.getBytesLength(keyBitSize);
        byte[] bytes = generateBytes(dKeyBytesLength);
        byte[] derivedKey = new byte[dKeyBytesLength];
        System.arraycopy(bytes, 0, derivedKey, 0, dKeyBytesLength);
        return new SimpleDerivedKey(derivedKey);
    }

    @Override
    public SimpleDerivedKey generateDerivedKeyWithIV(int keyBitSize, int ivBitSize) {
        Preconditions.checkArgument(keyBitSize > 0, "the keyBitSize must be greater than 0");
        Preconditions.checkArgument(ivBitSize > 0, "the ivBitSize must be greater than 0");
        int dKeyBytesLength = Securitys.getBytesLength(keyBitSize);
        int ivBytesLength = Securitys.getBytesLength(ivBitSize);
        byte[] bytes = generateBytes(dKeyBytesLength + ivBytesLength);
        byte[] derivedKey = new byte[dKeyBytesLength];
        byte[] iv = new byte[ivBytesLength];
        System.arraycopy(bytes, 0, derivedKey, 0, dKeyBytesLength);
        System.arraycopy(bytes, dKeyBytesLength, iv, 0, ivBytesLength);
        return new SimpleDerivedKey(derivedKey, iv);
    }

    @Override
    public SimpleDerivedKey generateDerivedKeyUseHMac(int keyBitSize) {
        return generateDerivedKey(keyBitSize);
    }

    private byte[] generateBytes(int bytesLength) {
        Preconditions.checkNotEmpty(this.digestAlgorithm);

        MessageDigest messageDigest = MessageDigests.getDigest(this.digestAlgorithm);

        if (Strings.equals(this.digestAlgorithm, "MD5", true) || Strings.equals(this.digestAlgorithm, "MD2", true)) {
            if (bytesLength > 16) {
                throw new IllegalArgumentException("the keyBitSize must be less than or equals 128");
            }
        } else {
            if (bytesLength > 20) {
                throw new IllegalArgumentException("the keyBitSize must be less than or equals 160");
            }
        }

        // iterations 1 times
        messageDigest.update(this.password);
        messageDigest.update(this.salt);
        byte[] bytes = messageDigest.digest();

        for (int currentIterationIndex = 1; currentIterationIndex < this.iterationCount; currentIterationIndex++) {
            bytes = messageDigest.digest(bytes);
        }

        return bytes;
    }
}
