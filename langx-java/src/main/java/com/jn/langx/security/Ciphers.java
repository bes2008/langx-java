package com.jn.langx.security;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;

/**
 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public class Ciphers {
    public static Cipher createEmptyCipher(@NonNull String algorithmTransformation, @Nullable Provider provider) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        Cipher cipher = provider == null ? Cipher.getInstance(algorithmTransformation) : Cipher.getInstance(algorithmTransformation, provider);
        return cipher;
    }

    public static Cipher createCipher(@NonNull String algorithmTransformation, @Nullable Provider provider, int operateMode, Key key, SecureRandom secureRandom) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = createEmptyCipher(algorithmTransformation, provider);
        if (secureRandom == null) {
            cipher.init(operateMode, key);
        } else {
            cipher.init(operateMode, key, secureRandom);
        }
        return cipher;
    }

    public static Cipher createCipher(@NonNull String algorithmTransformation, @Nullable Provider provider, int operateMode, Key key, AlgorithmParameterSpec parameterSpec, SecureRandom secureRandom) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = createEmptyCipher(algorithmTransformation, provider);
        if (secureRandom == null) {
            cipher.init(operateMode, key, parameterSpec);
        } else {
            cipher.init(operateMode, key, parameterSpec, secureRandom);
        }
        return cipher;
    }

    public static Cipher createCipher(@NonNull String algorithmTransformation, @Nullable Provider provider, int operateMode, Key key, AlgorithmParameters parameters, SecureRandom secureRandom) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = createEmptyCipher(algorithmTransformation, provider);
        if (secureRandom == null) {
            cipher.init(operateMode, key, parameters);
        } else {
            cipher.init(operateMode, key, parameters, secureRandom);
        }
        return cipher;
    }

    public static Cipher createCipher(@NonNull String algorithmTransformation, @Nullable Provider provider, int operateMode, Certificate certificate, SecureRandom secureRandom) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = createEmptyCipher(algorithmTransformation, provider);
        if (secureRandom == null) {
            cipher.init(operateMode, certificate);
        } else {
            cipher.init(operateMode, certificate, secureRandom);
        }
        return cipher;
    }
}
