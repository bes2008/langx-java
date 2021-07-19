package com.jn.langx.security.crypto.key;

import com.jn.langx.security.SecurityException;
import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.util.Strings;

import java.security.SecureRandom;

public class SecureRandoms {
    public static SecureRandom getNativePRNG() {
        try {
            return getSecureRandom(JCAEStandardName.NativePRNG.getName());
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static SecureRandom getSHA1PRNG() {
        try {
            return getSecureRandom(JCAEStandardName.SHA1PRNG.getName(), "SUN");
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static SecureRandom getSHA1PRNG(String provider) {
        try {
            return getSecureRandom(JCAEStandardName.SHA1PRNG.getName(), provider);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static SecureRandom getSecureRandom(String algorithm) {
        try {
            return Strings.isEmpty(algorithm) ? new SecureRandom() : SecureRandom.getInstance(algorithm);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static SecureRandom getSecureRandom(String algorithm, String provider) {
        try {
            return SecureRandom.getInstance(algorithm, provider);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }
}
