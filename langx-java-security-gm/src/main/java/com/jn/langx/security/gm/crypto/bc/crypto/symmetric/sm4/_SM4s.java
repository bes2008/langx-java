package com.jn.langx.security.gm.crypto.bc.crypto.symmetric.sm4;

import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.security.crypto.key.supplier.bytesbased.ByteBasedSecretKeySupplier;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.reflect.Reflects;

import javax.crypto.spec.IvParameterSpec;
import java.security.Provider;
import java.security.SecureRandom;

/**
 * 注意，在使用 CBC模式下使用时，传递的 secureRandom 必须是调用过 setSeed()之后的
 */
class _SM4s extends Symmetrics {
    public static final byte[] SECURE_RANDOM_SEED_DEFAULT = Reflects.getFQNClassName(_SM4s.class).getBytes(Charsets.UTF_8);
    public static byte[] encrypt(byte[] bytes, byte[] symmetricKey, Provider provider, SecureRandom secureRandom) {
        return encrypt(bytes, symmetricKey, "SM4", (String) null, provider, secureRandom);
    }

    public static byte[] encrypt(byte[] bytes, byte[] symmetricKey, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        IvParameterSpec ivParameterSpec = null;
        if (secureRandom == null) {
            try {
                secureRandom = SecureRandom.getInstance(JCAEStandardName.SHA1PRNG.getName());
                secureRandom.setSeed(SECURE_RANDOM_SEED_DEFAULT);
            } catch (Throwable ex) {
                throw Throwables.wrapAsRuntimeException(ex);
            }
        }
        if (Strings.contains(algorithmTransformation, "CBC")) {
            byte[] iv = PKIs.createSecretKey("SM4", provider == null ? null : provider.getName(), 128, secureRandom).getEncoded();
            ivParameterSpec = new IvParameterSpec(iv);
        }
        return encrypt(bytes, symmetricKey, "SM4", algorithmTransformation, provider, secureRandom, new ByteBasedSecretKeySupplier(), ivParameterSpec);
    }

    public static byte[] decrypt(byte[] bytes, byte[] symmetricKey, Provider provider, SecureRandom secureRandom) {
        return decrypt(bytes, symmetricKey, null, provider, secureRandom);
    }

    public static byte[] decrypt(byte[] bytes, byte[] symmetricKey, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        IvParameterSpec ivParameterSpec = null;
        if (secureRandom == null) {
            try {
                secureRandom = SecureRandom.getInstance(JCAEStandardName.SHA1PRNG.getName());
                secureRandom.setSeed(SECURE_RANDOM_SEED_DEFAULT);
            } catch (Throwable ex) {
                throw Throwables.wrapAsRuntimeException(ex);
            }
        }
        if (Strings.contains(algorithmTransformation, "CBC")) {
            byte[] iv = PKIs.createSecretKey("SM4", provider == null ? null : provider.getName(), 128, secureRandom).getEncoded();
            ivParameterSpec = new IvParameterSpec(iv);
        }
        return decrypt(bytes, symmetricKey, "SM4", algorithmTransformation, provider, secureRandom, new ByteBasedSecretKeySupplier(), ivParameterSpec);
    }
}
