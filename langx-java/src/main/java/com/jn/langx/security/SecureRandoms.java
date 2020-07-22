package com.jn.langx.security;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class SecureRandoms {
    public static SecureRandom getNativePRNG() throws NoSuchAlgorithmException, NoSuchProviderException {
        return getSecureRandom(JCAEStandardName.NativePRNG.getName());
    }

    public static SecureRandom getSHA1PRNG() throws NoSuchAlgorithmException, NoSuchProviderException {
        return getSecureRandom(JCAEStandardName.SHA1PRNG.getName(), "SUN");
    }

    public static SecureRandom getSHA1PRNG(String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        return getSecureRandom(JCAEStandardName.SHA1PRNG.getName(), provider);
    }

    public static SecureRandom getSecureRandom(String algorithm) throws NoSuchAlgorithmException, NoSuchProviderException {
        return SecureRandom.getInstance(algorithm);
    }

    public static SecureRandom getSecureRandom(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        return SecureRandom.getInstance(algorithm, provider);
    }
}
