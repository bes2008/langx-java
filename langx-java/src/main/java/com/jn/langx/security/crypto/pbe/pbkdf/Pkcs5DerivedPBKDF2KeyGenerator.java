package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.security.crypto.mac.HMacs;

import javax.crypto.Mac;
import java.security.MessageDigest;

/**
 * Generator for PBE derived keys and ivs as defined by PKCS 5 V2.0 Scheme 2.
 * This generator uses a SHA-1 HMac as the calculation function.
 * <p>
 * The document this implementation is based on can be found at
 * <a href=https://www.rsasecurity.com/rsalabs/pkcs/pkcs-5/index.html>
 * RSA's PKCS5 Page</a>
 */
public class Pkcs5DerivedPBKDF2KeyGenerator extends DerivedPBEKeyGenerator {
    private Mac hMac;
    private byte[] state;

    /**
     * construct a PKCS5 Scheme 2 Parameters generator.
     */
    public Pkcs5DerivedPBKDF2KeyGenerator() {
        this(MessageDigests.getDigest("SHA-1"));
    }

    public Pkcs5DerivedPBKDF2KeyGenerator(MessageDigest digest) {
        hMac = HMacs.createMac("Hmac" + digest.getAlgorithm());
        state = new byte[HMacs.getBlockLength(hMac)];
    }

    private void F(
            byte[] S,
            int c,
            byte[] iBuf,
            byte[] out,
            int outOff) {
        if (c == 0) {
            throw new IllegalArgumentException("iteration count must be at least 1.");
        }

        if (S != null) {
            hMac.update(S, 0, S.length);
        }

        hMac.update(iBuf, 0, iBuf.length);
        hMac.doFinal(state, 0);

        System.arraycopy(state, 0, out, outOff, state.length);

        for (int count = 1; count < c; count++) {
            hMac.update(state, 0, state.length);
            hMac.doFinal(state, 0);

            for (int j = 0; j != state.length; j++) {
                out[outOff + j] ^= state[j];
            }
        }
    }

    private byte[] generateDerivedKey(
            int dkLen) {
        int hLen = HMacs.getBlockLength(hMac);
        int l = (dkLen + hLen - 1) / hLen;
        byte[] iBuf = new byte[4];
        byte[] outBytes = new byte[l * hLen];
        int outPos = 0;

        CipherParameters param = new KeyParameter(password);

        hMac.init(param);

        for (int i = 1; i <= l; i++) {
            // Increment the value in 'iBuf'
            int pos = 3;
            while (++iBuf[pos] == 0) {
                --pos;
            }

            F(salt, iterationCount, iBuf, outBytes, outPos);
            outPos += hLen;
        }

        return outBytes;
    }

    /**
     * Generate a key parameter derived from the password, salt, and iteration
     * count we are currently initialised with.
     *
     * @param keySize the size of the key we want (in bits)
     * @return a KeyParameter object.
     */
    public DerivedPBEKey generateDerivedParameters(int keySize) {
        keySize = keySize / 8;

        byte[] dKey = generateDerivedKey(keySize);
        DerivedPBEKey dk = new DerivedPBEKey(getAlgorithmName(), getCipherAlgorithmName(), getKeySpec(), dKey);
        return new KeyParameter(dKey, 0, keySize);
    }

    /**
     * Generate a key with initialisation vector parameter derived from
     * the password, salt, and iteration count we are currently initialised
     * with.
     *
     * @param keySize the size of the key we want (in bits)
     * @param ivSize  the size of the iv we want (in bits)
     * @return a ParametersWithIV object.
     */
    public DerivedPBEKey generateDerivedParameters(
            int keySize,
            int ivSize) {
        keySize = keySize / 8;
        ivSize = ivSize / 8;

        byte[] dKey = generateDerivedKey(keySize + ivSize);

        return new ParametersWithIV(new KeyParameter(dKey, 0, keySize), dKey, keySize, ivSize);
    }

    /**
     * Generate a key parameter for use with a MAC derived from the password,
     * salt, and iteration count we are currently initialised with.
     *
     * @param keySize the size of the key we want (in bits)
     * @return a KeyParameter object.
     */
    public DerivedPBEKey generateDerivedMacParameters(int keySize) {
        return generateDerivedParameters(keySize);
    }
}
