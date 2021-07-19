package com.jn.langx.security.crypto.cipher;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.exception.IllegalParameterException;
import com.jn.langx.security.exception.SecurityException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

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

    /**
     * https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#trans
     * <p>
     * transformation 有两种形式：
     * <pre>
     *    1) 只有algorithm name
     *    2) 由3部分构成：{algorithm name}/{mode}/padding
     * </pre>
     */
    public static String createAlgorithmTransformation(String transformation) {
        Preconditions.checkNotEmpty(transformation, "the cipher algorithm transformation is null or empty");
        String[] components = Strings.split(transformation, "/");
        if (components.length == 0) {
            throw new IllegalParameterException(StringTemplates.formatWithPlaceholder("the cipher algorithm transformation is illegal: {}", transformation));
        }
        if (components.length < 3) {
            return components[0];
        }
        return createAlgorithmTransformation(components[0], components[1], components[2]);
    }

    public static String createAlgorithmTransformation(@NotEmpty String algorithm, @NotEmpty String mode, @NotEmpty String padding) {
        Preconditions.checkNotEmpty(algorithm, "the algorithm is null or empty");
        Preconditions.checkNotEmpty(mode, "the mode is null or empty");
        Preconditions.checkNotEmpty(padding, "the padding is null or empty");

        return StringTemplates.formatWithPlaceholder("{}/{}/{}", algorithm, mode, padding);
    }
}
