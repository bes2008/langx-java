package com.jn.langx.security.gm.crypto.bc.symmetric.sm4;

import com.jn.langx.security.crypto.cipher.AlgorithmParameterSupplier;
import com.jn.langx.security.crypto.cipher.Ciphers;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.reflect.Reflects;

import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.Provider;
import java.security.SecureRandom;

/**
 * SM4 在 非ECB 模式下，必须要用到的。
 */
public class SM4AlgorithmSpecSupplier implements AlgorithmParameterSupplier {
    private static final byte[] SECURE_RANDOM_SEED_DEFAULT = Reflects.getFQNClassName(SM4AlgorithmSpecSupplier.class).getBytes(Charsets.UTF_8);

    public SM4AlgorithmSpecSupplier() {
    }

    private byte[] iv;

    public SM4AlgorithmSpecSupplier(byte[] iv) {
        this.iv = iv;
    }

    @Override
    public Object get(Key key, String algorithm, String transform, Provider provider, SecureRandom secureRandom) {

        Symmetrics.MODE mode = Ciphers.extractSymmetricMode(transform);
        if (mode == null) {
            mode = Symmetrics.MODE.CBC;
        }
        if (!mode.hasIV()) {
            return null;
        }

        if (Emptys.isNotEmpty(iv)) {
            return new IvParameterSpec(iv);
        }

        // 生成 IV,因为 SM4的 iv 长度与 sm4的 iv 长度是一致的，都是128 bit的随机数，所以可以利用下面的方式生成 iv
        return Ciphers.createIvParameterSpec(null, SECURE_RANDOM_SEED_DEFAULT, 128);
    }

}
