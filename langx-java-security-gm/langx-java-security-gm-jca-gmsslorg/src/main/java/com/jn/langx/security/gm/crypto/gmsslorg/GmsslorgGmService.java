package com.jn.langx.security.gm.crypto.gmsslorg;

import com.jn.langx.security.crypto.AlgorithmUnregisteredException;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.gm.AbstractGmService;
import com.jn.langx.security.gm.GmService;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Maths;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.comparator.Comparators;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.logging.Loggers;
import org.gmssl.GmSSL;
import org.slf4j.Logger;

import java.util.Map;

public class GmsslorgGmService extends AbstractGmService {
    public static final String NAME = "GmSSL.org-GmService";
    private static final GmSSL gmssl;
    private static final Map<String, String> supportedCiphers;

    static {
        gmssl = new GmSSL();
        supportedCiphers = Pipeline.<String>of(gmssl.getCiphers())
                .collect(Collects.<String, String, String>toTreeMap(new Function<String, String>() {
                    @Override
                    public String apply(String key) {
                        return key.toUpperCase();
                    }
                }, new Function<String, String>() {
                    @Override
                    public String apply(String input) {
                        return input;
                    }
                }, Comparators.STRING_COMPARATOR_IGNORE_CASE));
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * GmSSL 在SM2 加解密这部分，只能用下面几个算法：
     * <pre>
     *     sm2encrypt-with-sm3,
     *     sm2encrypt-with-sha1,
     *     sm2encrypt-with-sha256,
     *     sm2encrypt-with-sha512
     * </pre>
     *
     * @param data
     * @param publicKey
     */
    @Override
    public byte[] sm2Encrypt(byte[] data, byte[] publicKey) {
        return gmssl.publicKeyEncrypt("sm2encrypt-with-sm3", data, publicKey);
    }

    @Override
    public byte[] sm2Decrypt(byte[] encryptedBytes, byte[] privateKey) {
        byte[] data = gmssl.publicKeyDecrypt("sm2encrypt-with-sm3", encryptedBytes, privateKey);
        if (data == null) {
            logErrors();
        }
        return data;
    }

    @Override
    public byte[] sm2Sign(byte[] data, byte[] privateKey) {
        return gmssl.sign("sm2sign", data, privateKey);
    }

    @Override
    public boolean sm2Verify(byte[] data, byte[] publicKey, byte[] signature) {
        int ret = gmssl.verify("sm2sign", data, signature, publicKey);
        return ret == 1;
    }

    @Override
    public byte[] sm3(byte[] data) {
        return sm3(data, 1);
    }

    @Override
    public byte[] sm3(byte[] data, int iterations) {
        return sm3(data, null, iterations);
    }

    @Override
    public byte[] sm3(byte[] data, byte[] salt, int iterations) {
        byte[] bytes = data;
        if (Emptys.isNotEmpty(salt)) {
            bytes = gmssl.digest("SM3", salt);
        }
        iterations = Maths.max(1, iterations);
        for (int i = 0; i < iterations; i++) {
            bytes = gmssl.digest("SM3", bytes);
        }
        return bytes;
    }

    @Override
    public byte[] sm4Encrypt(byte[] data, byte[] secretKey) {
        return sm4Encrypt(data, (Symmetrics.MODE) null, secretKey);
    }

    @Override
    public byte[] sm4Encrypt(byte[] data, Symmetrics.MODE mode, byte[] secretKey) {
        return sm4Encrypt(data, mode, secretKey, SM4_IV_DEFAULT);
    }

    /**
     * @param data
     * @param mode      目前只能用 CBC模式
     * @param secretKey
     * @param iv
     * @return 加密后的值
     * <p>
     * SM4 默认采用 PKC#7 padding
     * 目前ECB模式不可用，会core dump
     */
    @Override
    public byte[] sm4Encrypt(byte[] data, Symmetrics.MODE mode, byte[] secretKey, byte[] iv) {
        if (mode == null) {
            mode = Symmetrics.MODE.CBC;
        }
        String cipher=null;
        if (supportedCiphers.containsKey("SM4-"+mode.name())) {
            cipher = "SM4-"+mode.name();
        }else if(supportedCiphers.containsKey("SMS4-"+mode.name())){
            cipher = "SMS4-"+mode.name();
        }
        if (Emptys.isEmpty(cipher)) {
            throw new AlgorithmUnregisteredException("SM4-"+mode.name()+"SMS4-"+mode.name());
        }
        if (Emptys.isEmpty(iv)) {
            iv = GmService.SM4_IV_DEFAULT;
        }
        iv = Arrs.copy(iv);
        // 目前 ECB 模式会返回 null, 这是有Bug的，
        byte[] bytes = gmssl.symmetricEncrypt(cipher, data, secretKey, iv);
        if (bytes == null) {
            logErrors();
        }
        return bytes;
    }

    public byte[] sm4Decrypt(byte[] encryptedBytes, byte[] secretKey) {
        return sm4Decrypt(encryptedBytes, (Symmetrics.MODE) null, secretKey);
    }

    @Override
    public byte[] sm4Decrypt(byte[] encryptedBytes, Symmetrics.MODE mode, byte[] secretKey) {
        return sm4Decrypt(encryptedBytes, mode, secretKey, SM4_IV_DEFAULT);
    }

    @Override
    public byte[] sm4Decrypt(byte[] encryptedBytes, Symmetrics.MODE mode, byte[] secretKey, byte[] iv) {
        if (mode == null) {
            mode = Symmetrics.MODE.CBC;
        }
        String cipher=null;
        if (supportedCiphers.containsKey("SM4-"+mode.name())) {
            cipher = "SM4-"+mode.name();
        }else if(supportedCiphers.containsKey("SMS4-"+mode.name())){
            cipher = "SMS4-"+mode.name();
        }
        if (Emptys.isEmpty(cipher)) {
            throw new AlgorithmUnregisteredException("SM4-"+mode.name()+"SMS4-"+mode.name());
        }
        if (Emptys.isEmpty(iv)) {
            iv = GmService.SM4_IV_DEFAULT;
        }
        iv = Arrs.copy(iv);
        return gmssl.symmetricDecrypt(cipher, encryptedBytes, secretKey, iv);
    }

    private void logErrors() {
        String[] errors = gmssl.getErrorStrings();
        if (errors != null) {
            final Logger logger = Loggers.getLogger(getClass());
            Collects.forEach(gmssl.getErrorStrings(), new Consumer<String>() {
                @Override
                public void accept(String s) {
                    logger.error(s);
                }
            });
        }
    }
}
