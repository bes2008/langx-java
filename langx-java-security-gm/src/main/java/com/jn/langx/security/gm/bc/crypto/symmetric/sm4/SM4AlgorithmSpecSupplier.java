package com.jn.langx.security.gm.bc.crypto.symmetric.sm4;

import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.cipher.AlgorithmParameterSupplier;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.reflect.Reflects;

import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.Provider;
import java.security.SecureRandom;

/**
 * SM4 在 CBC 模式下，必须要用到的。
 */
public class SM4AlgorithmSpecSupplier implements AlgorithmParameterSupplier {
    public static final byte[] SECURE_RANDOM_SEED_DEFAULT = Reflects.getFQNClassName(SM4AlgorithmSpecSupplier.class).getBytes(Charsets.UTF_8);

    @Override
    public Object get(Key key, String algorithm, String transform, Provider provider, SecureRandom secureRandom) {
        if(Strings.contains(transform, "CBC")) {
            IvParameterSpec ivParameterSpec = null;
            if (secureRandom == null) {
                try {
                    secureRandom = SecureRandom.getInstance(JCAEStandardName.SHA1PRNG.getName());
                    secureRandom.setSeed(SECURE_RANDOM_SEED_DEFAULT);
                } catch (Throwable ex) {
                    throw Throwables.wrapAsRuntimeException(ex);
                }
            }

            byte[] iv = PKIs.createSecretKey("SM4", provider == null ? null : provider.getName(), 128, secureRandom).getEncoded();
            ivParameterSpec = new IvParameterSpec(iv);

            return ivParameterSpec;
        }
        return null;
    }

}
