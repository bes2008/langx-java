package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.digest.DigestSizeAware;
import com.jn.langx.security.crypto.digest.internal.Digest;
import com.jn.langx.security.crypto.digest.internal.Digests;

public class PKCS12DerivedKeyGenerator extends DerivedKeyGenerator {
    public static final int KEY_MATERIAL = 1;
    public static final int IV_MATERIAL = 2;
    public static final int MAC_MATERIAL = 3;

    private Digest digest;

    private int u;
    private int v;

    /**
     * Construct a PKCS 12 Parameters generator. This constructor will
     * accept any digest which also implements ExtendedDigest.
     *
     * @param digestAlgorithm the digest to be used as the source of derived keys.
     * @throws IllegalArgumentException if an unknown digest is passed in.
     */
    public PKCS12DerivedKeyGenerator(String digestAlgorithm) {
        this.digest = Digests.getDigest(digestAlgorithm);
        if (digest instanceof DigestSizeAware) {
            u = digest.getDigestSize();
            v = digest.getByteLength();
        } else {
            throw new IllegalArgumentException("Digest " + digest.getAlgorithmName() + " unsupported");
        }
    }

    /**
     * add a + b + 1, returning the result in a. The a value is treated
     * as a BigInteger of length (b.length * 8) bits. The result is
     * modulo 2^b.length in case of overflow.
     */
    private void adjust(
            byte[] a,
            int aOff,
            byte[] b) {
        int x = (b[b.length - 1] & 0xff) + (a[aOff + b.length - 1] & 0xff) + 1;

        a[aOff + b.length - 1] = (byte) x;
        x >>>= 8;

        for (int i = b.length - 2; i >= 0; i--) {
            x += (b[i] & 0xff) + (a[aOff + i] & 0xff);
            a[aOff + i] = (byte) x;
            x >>>= 8;
        }
    }

    /**
     * generation of a derived key ala PKCS12 V1.0.
     */
    private byte[] generateDerivedKey(
            int idByte,
            int n) {
        byte[] D = new byte[v];
        byte[] dKey = new byte[n];

        for (int i = 0; i != D.length; i++) {
            D[i] = (byte) idByte;
        }

        byte[] S;

        if ((salt != null) && (salt.length != 0)) {
            S = new byte[v * ((salt.length + v - 1) / v)];

            for (int i = 0; i != S.length; i++) {
                S[i] = salt[i % salt.length];
            }
        } else {
            S = new byte[0];
        }

        byte[] P;

        if ((password != null) && (password.length != 0)) {
            P = new byte[v * ((password.length + v - 1) / v)];

            for (int i = 0; i != P.length; i++) {
                P[i] = password[i % password.length];
            }
        } else {
            P = new byte[0];
        }

        byte[] I = new byte[S.length + P.length];

        System.arraycopy(S, 0, I, 0, S.length);
        System.arraycopy(P, 0, I, S.length, P.length);

        byte[] B = new byte[v];
        int c = (n + u - 1) / u;
        byte[] A = new byte[u];

        for (int i = 1; i <= c; i++) {
            digest.update(D, 0, D.length);
            digest.update(I, 0, I.length);
            digest.doFinal(A, 0);
            for (int j = 1; j < iterationCount; j++) {
                digest.update(A, 0, A.length);
                digest.doFinal(A, 0);
            }

            for (int j = 0; j != B.length; j++) {
                B[j] = A[j % A.length];
            }

            for (int j = 0; j != I.length / v; j++) {
                adjust(I, j * v, B);
            }

            if (i == c) {
                System.arraycopy(A, 0, dKey, (i - 1) * u, dKey.length - ((i - 1) * u));
            } else {
                System.arraycopy(A, 0, dKey, (i - 1) * u, A.length);
            }
        }

        return dKey;
    }

    /**
     * Generate a key parameter derived from the password, salt, and iteration
     * count we are currently initialised with.
     *
     * @param keyBitSize the size of the key we want (in bits)
     * @return a KeyParameter object.
     */
    @Override
    public SimpleDerivedKey generateDerivedKey(int keyBitSize) {
        int keyBytesLength = Securitys.getBytesLength(keyBitSize);
        byte[] dKey = generateDerivedKey(KEY_MATERIAL, keyBytesLength);
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
    @Override
    public SimpleDerivedKey generateDerivedKeyWithIV(int keyBitSize, int ivBitSize) {
        int keyBytesLength = Securitys.getBytesLength(keyBitSize);
        int ivBytesLength = Securitys.getBytesLength(ivBitSize);
        byte[] keyBytes = generateDerivedKey(KEY_MATERIAL, keyBytesLength);
        byte[] ivBytes = generateDerivedKey(IV_MATERIAL, ivBytesLength);

        byte[] derivedKey = new byte[keyBytesLength];
        System.arraycopy(keyBytes, 0, derivedKey, 0, keyBytesLength);
        byte[] iv = new byte[ivBytesLength];
        System.arraycopy(ivBytes, 0, iv, 0, ivBytesLength);
        return new SimpleDerivedKey(derivedKey, iv);
    }

    /**
     * Generate a key parameter for use with a MAC derived from the password,
     * salt, and iteration count we are currently initialised with.
     *
     * @param keyBitSize the size of the key we want (in bits)
     * @return a KeyParameter object.
     */
    @Override
    public SimpleDerivedKey generateDerivedKeyUseHMac(int keyBitSize) {
        int keyBytesLength = Securitys.getBytesLength(keyBitSize);
        byte[] dKey = generateDerivedKey(MAC_MATERIAL, keyBytesLength);
        byte[] derivedKey = new byte[keyBytesLength];
        System.arraycopy(dKey, 0, derivedKey, 0, keyBytesLength);
        return new SimpleDerivedKey(derivedKey);
    }
}
