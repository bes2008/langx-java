package com.jn.langx.security;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.security.exception.SecurityException;

import javax.crypto.Cipher;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;

/**
 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public class Ciphers {
    public static Cipher createEmptyCipher(@NonNull String algorithmTransformation, @Nullable Provider provider) {
        try {
            Cipher cipher = provider == null ? Cipher.getInstance(algorithmTransformation) : Cipher.getInstance(algorithmTransformation, provider);
            return cipher;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static Cipher createCipher(@NonNull String algorithmTransformation, @Nullable Provider provider, int operateMode, Key key, SecureRandom secureRandom) {
        try {
            Cipher cipher = createEmptyCipher(algorithmTransformation, provider);
            if (secureRandom == null) {
                cipher.init(operateMode, key);
            } else {
                cipher.init(operateMode, key, secureRandom);
            }
            return cipher;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static Cipher createCipher(@NonNull String algorithmTransformation, @Nullable Provider provider, int operateMode, Key key, @Nullable AlgorithmParameterSpec parameterSpec, SecureRandom secureRandom) {
        try {
            Cipher cipher = createEmptyCipher(algorithmTransformation, provider);
            if (secureRandom == null) {
                cipher.init(operateMode, key, parameterSpec);
            } else {
                cipher.init(operateMode, key, parameterSpec, secureRandom);
            }
            return cipher;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static Cipher createCipher(@NonNull String algorithmTransformation, @Nullable Provider provider, int operateMode, Key key, @Nullable AlgorithmParameters parameters, SecureRandom secureRandom) {
        try {
            Cipher cipher = createEmptyCipher(algorithmTransformation, provider);
            if (secureRandom == null) {
                cipher.init(operateMode, key, parameters);
            } else {
                cipher.init(operateMode, key, parameters, secureRandom);
            }
            return cipher;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static Cipher createCipher(@NonNull String algorithmTransformation, @Nullable Provider provider, int operateMode, Certificate certificate, SecureRandom secureRandom) {
        try {
            Cipher cipher = createEmptyCipher(algorithmTransformation, provider);
            if (secureRandom == null) {
                cipher.init(operateMode, certificate);
            } else {
                cipher.init(operateMode, certificate, secureRandom);
            }
            return cipher;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static byte[] encrypt(Cipher cipher, byte[] data) {
        try {
            return cipher.doFinal(data);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static byte[] decrypt(Cipher cipher, byte[] data) {
        try {
            return cipher.doFinal(data);
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }
}
