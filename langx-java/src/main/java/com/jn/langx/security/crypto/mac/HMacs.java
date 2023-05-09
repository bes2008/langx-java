package com.jn.langx.security.crypto.mac;

import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.CryptoException;
import com.jn.langx.security.crypto.key.PKIs;
import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.Strings;
import com.jn.langx.util.reflect.Reflects;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.MacSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

public class HMacs extends Securitys {
    /**
     * key: message digest algorithm
     * value: block length
     */
    private static ConcurrentHashMap<String, Integer> blockLengths = new ConcurrentHashMap<String, Integer>();

    static {
        blockLengths.put("GOST3411", 32);

        blockLengths.put("MD2", 16);
        blockLengths.put("MD4", 64);
        blockLengths.put("MD5", 64);

        blockLengths.put("RIPEMD128", 64);
        blockLengths.put("RIPEMD160", 64);

        blockLengths.put("SHA-1", 64);
        blockLengths.put("SHA-224", 64);
        blockLengths.put("SHA-256", 64);
        blockLengths.put("SHA-384", 128);
        blockLengths.put("SHA-512", 128);

        blockLengths.put("SM3", 64);

        blockLengths.put("Tiger", 64);
        blockLengths.put("Whirlpool", 64);
    }

    public static int getBlockLength(String messageDigestAlgorithm) {
        Integer length = blockLengths.get(messageDigestAlgorithm);
        if (length == null) {
            return -1;
        }
        return length;
    }

    public static Mac createMac(String algorithm) {
        Mac mac;
        try {
            mac = Mac.getInstance(algorithm);
            return mac;
        } catch (Throwable ex) {
            if (ex instanceof NoSuchAlgorithmException) {
                if (Strings.startsWith(algorithm, "hmac", true)) {
                    String macSpiClassName = Securitys.getLangxSecurityProvider().findAlgorithm("Mac", algorithm);
                    if (ClassLoaders.hasClass(macSpiClassName, PKIs.class.getClassLoader())) {
                        try {
                            Class macSpiClass = ClassLoaders.loadClass(macSpiClassName, PKIs.class.getClassLoader());
                            MacSpi macSpi = Reflects.<MacSpi>newInstance(macSpiClass);
                            mac = new LangxMac(macSpi, Securitys.getLangxSecurityProvider(), algorithm);
                            return mac;
                        } catch (Throwable ex2) {
                            // ignore it
                        }
                    }
                }
            }

            throw new CryptoException(ex);
        }
    }

    public static Mac createMac(String algorithm, byte[] secretKey) {
        SecretKey key;
        if (secretKey == null) {
            KeyGenerator keyGenerator = PKIs.getKeyGenerator(algorithm, null);
            key = keyGenerator.generateKey();
        } else {
            key = new SecretKeySpec(secretKey, algorithm);
        }
        return createMac(algorithm, key);
    }

    public static Mac createMac(String algorithm, SecretKey key) {
        Mac mac = createMac(algorithm);
        try {
            mac.init(key);
        } catch (Throwable ex) {
            throw new CryptoException(ex);
        }
        return mac;
    }

    public static byte[] hmac(String algorithm, byte[] secretKey, byte[] data) {
        SecretKey key = new SecretKeySpec(secretKey, algorithm);
        return hmac(algorithm, key, data);
    }

    public static byte[] hmac(String algorithm, SecretKey key, byte[] data) {
        Mac mac = createMac(algorithm, key);
        try {
            return mac.doFinal(data);
        } catch (Throwable ex) {
            throw new CryptoException(ex);
        }
    }
}
