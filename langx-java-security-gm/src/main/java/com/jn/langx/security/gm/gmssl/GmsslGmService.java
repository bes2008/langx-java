package com.jn.langx.security.gm.gmssl;

import com.jn.langx.security.gm.GmService;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Maths;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import org.gmssl.GmSSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GmsslGmService implements GmService {
    public static final String NAME = "GmSSL-GmService";
    private static final Logger logger = LoggerFactory.getLogger(GmsslGmService.class);
    private final GmSSL gmssl = new GmSSL();

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
     * @return
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
        return sm4Encrypt(data, "CBC", secretKey);
    }

    @Override
    public byte[] sm4Encrypt(byte[] data, String mode, byte[] secretKey) {
        return sm4Encrypt(data, mode, secretKey, SM4_IV_DEFAULT);
    }

    /**
     * @param data
     * @param mode      目前只能用 CBC模式
     * @param secretKey
     * @param iv
     * @return 加密后的值
     * <p>
     * SMS4 默认采用 PKC#7 padding
     */
    @Override
    public byte[] sm4Encrypt(byte[] data, String mode, byte[] secretKey, byte[] iv) {
        if (Emptys.isEmpty(mode)) {
            mode = "CBC";
        }
        String cipher = "SMS4-" + mode.toUpperCase();
        if (Emptys.isEmpty(iv)) {
            iv = GmService.SM4_IV_DEFAULT;
        }
        // 目前 ECB 模式会返回 null, 这是有Bug的，
        byte[] bytes = gmssl.symmetricEncrypt(cipher, data, secretKey, iv);
        if (bytes == null) {
            logErrors();
        }
        return bytes;
    }

    public byte[] sm4Decrypt(byte[] encryptedBytes, byte[] secretKey) {
        return sm4Decrypt(encryptedBytes, null, secretKey);
    }

    @Override
    public byte[] sm4Decrypt(byte[] encryptedBytes, String mode, byte[] secretKey) {
        return sm4Decrypt(encryptedBytes, mode, secretKey, SM4_IV_DEFAULT);
    }

    @Override
    public byte[] sm4Decrypt(byte[] encryptedBytes, String mode, byte[] secretKey, byte[] iv) {
        if (Emptys.isEmpty(mode)) {
            mode = "CBC";
        }
        String cipher = "SMS4-" + mode.toUpperCase();
        if (cipher.contains("-CBC")) {
            if (Emptys.isEmpty(iv)) {
                iv = GmService.SM4_IV_DEFAULT;
            }
        }
        return gmssl.symmetricDecrypt(cipher, encryptedBytes, secretKey, iv);
    }

    private void logErrors() {
        String[] errors = gmssl.getErrorStrings();
        if (errors != null) {
            Collects.forEach(gmssl.getErrorStrings(), new Consumer<String>() {
                @Override
                public void accept(String s) {
                    logger.error(s);
                }
            });
        }
    }
}
