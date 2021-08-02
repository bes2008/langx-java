package com.jn.langx.security.gm.gmssl;

import com.jn.langx.security.gm.GmService;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Maths;
import org.gmssl.GmSSL;

public class GmsslGmService implements GmService {
    public static final String NAME = "GmSSL-GmService";
    private final GmSSL gmssl = new GmSSL();

    @Override
    public String getName() {
        return NAME;
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
     *
     * SMS4 默认采用 PKC#7 padding
     */
    @Override
    public byte[] sm4Encrypt(byte[] data, String mode, byte[] secretKey, byte[] iv) {
        if (Emptys.isEmpty(mode)) {
            mode = "ECB";
        }
        String cipher = "SMS4-" + mode.toUpperCase();
        if (Emptys.isEmpty(iv)) {
            iv = GmService.SM4_IV_DEFAULT;
        }
        // 目前 ECB 模式会返回 null
        return gmssl.symmetricEncrypt(cipher, data, secretKey, iv);
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
            mode = "ECB";
        }
        String cipher = "SMS4-" + mode.toUpperCase();
        if (cipher.contains("-CBC")) {
            if (Emptys.isEmpty(iv)) {
                iv = GmService.SM4_IV_DEFAULT;
            }
        }
        return gmssl.symmetricDecrypt(cipher, encryptedBytes, secretKey, iv);
    }
}
