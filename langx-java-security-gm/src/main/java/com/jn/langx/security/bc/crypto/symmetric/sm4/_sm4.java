package com.jn.langx.security.bc.crypto.symmetric.sm4;


import com.jn.langx.codec.base64.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

class _sm4 {

    private static Logger logger = LoggerFactory.getLogger(_sm4.class);

    private static final String PROVIDER_NAME = "BC";
    public static final String ALGORITHM_NAME = "SM4";
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    public static final String ALGORITHM_NAME_CBC_PADDING = "SM4/CBC/PKCS5Padding";
    public static final String DEFAULT_KEY = "random_seed";
    public static final int DEFAULT_KEY_SIZE = 128;
    private static final int ENCRYPT_MODE = 1;
    private static final int DECRYPT_MODE = 2;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] generateKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        return generateKey(DEFAULT_KEY, DEFAULT_KEY_SIZE);
    }

    public static byte[] generateKey(String seed) throws NoSuchAlgorithmException, NoSuchProviderException {
        return generateKey(seed, DEFAULT_KEY_SIZE);
    }

    public static byte[] generateKey(String seed, int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, PROVIDER_NAME);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        if (null != seed && !"".equals(seed)) {
            random.setSeed(seed.getBytes());
        }
        kg.init(keySize, random);
        return kg.generateKey().getEncoded();
    }

    /**
     * ecb 加密
     *
     * @param key
     * @param data
     */
    public static byte[] encryptEcbPadding(byte[] key, byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = generateEcbCipher(ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * ecb 解密
     *
     * @param key
     * @param cipherText
     */
    public static byte[] decryptEcbPadding(byte[] key, byte[] cipherText) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        Cipher cipher = generateEcbCipher(DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    /**
     * cbc 加密
     *
     * @param key
     * @param data
     */
    public static byte[] encryptCbcPadding(byte[] key, byte[] iv, byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = generateCbcCipher(ENCRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    public static String encryptCbcPaddingString(byte[] key, byte[] iv, byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = generateCbcCipher(ENCRYPT_MODE, key, iv);
        byte[] result = cipher.doFinal(data);
        return Base64.encodeBase64ToString(result);
    }

    /**
     * cbc 解密
     *
     * @param key
     * @param iv
     * @param cipherText
     */
    public static byte[] decryptCbcPadding(byte[] key, byte[] iv, String cipherText) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        byte[] cipherBytes = Base64.decodeBase64(cipherText);
        Cipher cipher = generateCbcCipher(DECRYPT_MODE, key, iv);
        return cipher.doFinal(cipherBytes);
    }

    public static byte[] decryptCbcPadding(byte[] key, byte[] iv, byte[] cipherText) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = generateCbcCipher(DECRYPT_MODE, key, iv);
        return cipher.doFinal(cipherText);
    }

    /**
     * ecb cipher
     *
     * @param mode
     * @param key
     * @return
     */
    private static Cipher generateEcbCipher(int mode, byte[] key) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING, PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    /**
     * cbc cipher
     *
     * @param mode
     * @param key
     * @return
     */
    private static Cipher generateCbcCipher(int mode, byte[] key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_CBC_PADDING, PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(mode, sm4Key, ivParameterSpec);
        return cipher;
    }

    /**
     * ecb 加密 times 次
     *
     * @param data
     * @param salt
     * @param times
     * @return=
     */
    public static String encryptEcbDataTimes(String data, String salt, int times) throws GeneralSecurityException {
        try {
            byte[] key = Hex.decode(salt);
            byte[] bytes = data.getBytes();

            for (int i = 0; i < times; ++i) {
                bytes = encryptEcbPadding(key, bytes);
            }

            data = Base64.encodeBase64String(bytes);
            return data;
        } catch (Throwable var5) {
            throw new GeneralSecurityException("SM4加密失败");
        }
    }

    /**
     * ecb 解密 times 次
     *
     * @param data
     * @param salt
     * @param times
     * @return
     * @throws GeneralSecurityException
     */
    public static String decryptEcbDataTimes(String data, String salt, int times) throws GeneralSecurityException {
        try {
            byte[] bytes = Base64.decodeBase64(data);
            byte[] key = Hex.decode(salt);

            for (int i = 0; i < times; ++i) {
                bytes = decryptEcbPadding(key, bytes);
            }

            data = new String(bytes);
            return data;
        } catch (Throwable ex) {
            throw new GeneralSecurityException("SM4解密失败");
        }
    }

    /**
     * cbc 加密 times 次
     *
     * @param data
     * @param salt
     * @param times
     * @return=
     */
    public static String encryptCbcDataTimes(String data, String salt, int times) {
        try {
            byte[] iv = generateKey();
            byte[] key = generateKey(salt);
            byte[] bytes = data.getBytes();

            Cipher cipher = generateCbcCipher(ENCRYPT_MODE, key, iv);
            for (int i = 0; i < times; ++i) {
                bytes = cipher.doFinal(bytes);
            }

            data = Base64.encodeBase64String(bytes);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * cbc 解密 times 次
     *
     * @param data
     * @param salt
     * @param times
     * @return
     * @throws GeneralSecurityException
     */
    public static String decryptCbcDataTimes(String data, String salt, int times) throws GeneralSecurityException {
        try {
            byte[] iv = generateKey();
            byte[] bytes = Base64.decodeBase64(data);
            byte[] key = generateKey(salt);

            Cipher cipher = generateCbcCipher(DECRYPT_MODE, key, iv);
            for (int i = 0; i < times; ++i) {
                bytes = cipher.doFinal(bytes);
            }

            data = new String(bytes);
            return data;
        } catch (Throwable var5) {
            throw new GeneralSecurityException("SM4解密失败, error: {}", var5);
        }
    }
}