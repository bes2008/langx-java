package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.SecurityException;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.security.crypto.mac.HMacKey;
import com.jn.langx.security.crypto.mac.HMacs;
import com.jn.langx.util.Chars;

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

    private void F(byte[] salt, int iterations, byte[] iBuf, byte[] out, int outOff) {

        if (iterations == 0) {
            throw new IllegalArgumentException("iteration count must be at least 1.");
        }
        try {
            if (salt != null) {
                hMac.update(salt, 0, salt.length);
            }

            hMac.update(iBuf, 0, iBuf.length);
            hMac.doFinal(state, 0);

            System.arraycopy(state, 0, out, outOff, state.length);

            for (int count = 1; count < iterations; count++) {
                hMac.update(state, 0, state.length);
                hMac.doFinal(state, 0);

                for (int j = 0; j != state.length; j++) {
                    out[outOff + j] ^= state[j];
                }
            }
        } catch (Throwable ex) {
            throw new SecurityException("derived key failed:" + ex.getMessage(), ex);
        }
    }

    private byte[] generateDerivedKey(int dkLen) {
        try {
            int hLen = HMacs.getBlockLength(hMac);
            int l = (dkLen + hLen - 1) / hLen;
            byte[] iBuf = new byte[4];
            byte[] outBytes = new byte[l * hLen];
            int outPos = 0;

            HMacKey hMacKey = new HMacKey(password);
            hMac.init(hMacKey);

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
        } catch (Throwable e) {
            throw new SecurityException("derived key failed", e);
        }
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

        PBKDFKeySpec pbkdfKeySpec = new PBKDFKeySpec(Chars.utf8BytesToChars(password), salt, keySize * 8, 0, iterationCount);

        DerivedPBEKey dk = new DerivedPBEKey("PBKDF2-HMAC-" + HMacs.getDigestAlgorithm(this.hMac), pbkdfKeySpec, dKey);
        return dk;
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
    public DerivedPBEKey generateDerivedParameters(int keySize, int ivSize) {
        keySize = keySize / 8;
        ivSize = ivSize / 8;


        byte[] dKey = generateDerivedKey(keySize + ivSize);
        PBKDFKeySpec pbkdfKeySpec = new PBKDFKeySpec(Chars.utf8BytesToChars(password), salt, keySize * 8, ivSize * 8, iterationCount);
        DerivedPBEKey dk = new DerivedPBEKey("PBKDF2-HMAC-" + HMacs.getDigestAlgorithm(this.hMac), pbkdfKeySpec, dKey);
        return dk;
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
