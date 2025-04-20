package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.SecurityException;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.digest.MessageDigests;
import com.jn.langx.security.crypto.mac.HMacKey;
import com.jn.langx.security.crypto.mac.HMacs;

import javax.crypto.Mac;
import java.security.MessageDigest;

/**
 * Generator for PBE derived keys and ivs as defined by PKCS 5 V2.0 Scheme 2.
 * This generator uses a SHA-1 HMac as the calculation function.
 * <p>
 * The document this implementation is based on can be found at
 * <a href="https://www.rsasecurity.com/rsalabs/pkcs/pkcs-5/index.html">RSA's PKCS5 Page</a>,
 * <a href="https://www.rfc-editor.org/rfc/rfc8018#section-5.2">rfc8018</a>.
 *
 * @see <a href="org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator">PKCS5S2ParametersGenerator</a>
 * @since 5.5.0
 */
public class PBKDF2DerivedKeyGenerator extends DerivedKeyGenerator {
    private Mac hMac;
    private byte[] state;

    /**
     * construct a PKCS5 Scheme 2 Parameters generator.
     */
    public PBKDF2DerivedKeyGenerator() {
        this("HmacSHA1");
    }

    public PBKDF2DerivedKeyGenerator(String hmacAlgorithm) {
        setHmacAlgorithm(hmacAlgorithm);
    }

    @Override
    public void init(byte[] password, byte[] salt, int iterationCount) {
        super.init(password, salt, iterationCount);
        if (iterationCount < 1) {
            this.iterationCount = 1;
        }

        if (salt.length < 8) {
            throw new IllegalArgumentException("salt length must be at least 8 bytes.");
        }

    }

    public void setHmacAlgorithm(String hmacAlgorithm) {
        hMac = HMacs.createMac(hmacAlgorithm);
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

    private byte[] generateDerivedKeyInternal(int dkLen) {
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
     * @param keyBitSize the size of the key we want (in bits)
     * @return a KeyParameter object.
     */
    public SimpleDerivedKey generateDerivedKey(int keyBitSize) {
        int keyBytesLength = Securitys.getBytesLength(keyBitSize);

        byte[] dKey = generateDerivedKeyInternal(keyBytesLength);
        byte[] derivedKey = new byte[keyBytesLength];
        System.arraycopy(dKey, 0, derivedKey, 0, keyBytesLength);
        return new SimpleDerivedKey(derivedKey);
    }

    /**
     * Generate a key with initialisation vector parameter derived from
     * the password, salt, and iteration count we are currently initialised
     * with.
     *
     * @param keyBitSize the size of the key we want (in bits)
     * @param ivBitSize  the size of the iv we want (in bits)
     * @return a ParametersWithIV object.
     */
    public SimpleDerivedKey generateDerivedKeyWithIV(int keyBitSize, int ivBitSize) {
        int keyBytesLength = Securitys.getBytesLength(keyBitSize);
        int ivBytesLength = Securitys.getBytesLength(ivBitSize);

        byte[] dKey = generateDerivedKeyInternal(keyBytesLength + ivBytesLength);
        byte[] key = new byte[keyBytesLength];
        byte[] iv = new byte[ivBytesLength];
        System.arraycopy(dKey, 0, key, 0, keyBytesLength);
        System.arraycopy(dKey, keyBytesLength, iv, 0, ivBytesLength);
        return new SimpleDerivedKey(key, iv);
    }

    /**
     * Generate a key parameter for use with a MAC derived from the password,
     * salt, and iteration count we are currently initialised with.
     *
     * @param keySize the size of the key we want (in bits)
     * @return a KeyParameter object.
     */
    public SimpleDerivedKey generateDerivedKeyUseHMac(int keySize) {
        return generateDerivedKey(keySize);
    }
}
