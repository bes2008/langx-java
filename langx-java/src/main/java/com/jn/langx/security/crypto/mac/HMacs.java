package com.jn.langx.security.crypto.mac;

import java.util.concurrent.ConcurrentHashMap;

public class HMacs {
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
}
