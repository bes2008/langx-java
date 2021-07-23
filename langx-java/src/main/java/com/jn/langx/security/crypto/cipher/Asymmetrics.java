package com.jn.langx.security.crypto.cipher;

import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPrivateKeySupplier;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPublicKeySupplier;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

import java.security.Provider;
import java.security.SecureRandom;

public class Asymmetrics extends Ciphers {
    protected Asymmetrics() {
    }

    public static byte[] encrypt(byte[] bytes, byte[] pubKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        return encrypt(bytes, pubKey, algorithm, algorithmTransformation, provider, secureRandom, new BytesBasedPublicKeySupplier());
    }


    public static byte[] encryptUsePriKey(byte[] bytes, byte[] priKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        if("SM2".equals(algorithm)){
            throw new UnsupportedOperationException("SM2 is not supports to encrypt use private key");
        }
        return encrypt(bytes, priKey, algorithm, algorithmTransformation, provider, secureRandom, new BytesBasedPrivateKeySupplier());
    }


    public static byte[] decrypt(byte[] bytes, byte[] priKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        return decrypt(bytes, priKey, algorithm, algorithmTransformation, provider, secureRandom, new BytesBasedPrivateKeySupplier());
    }

    public static byte[] decryptUsePubKey(byte[] bytes, byte[] pubKey, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        if("SM2".equals(algorithm)){
            throw new UnsupportedOperationException("SM2 is not supports to decrypt use public key");
        }
        return decrypt(bytes, pubKey, algorithm, algorithmTransformation, provider, secureRandom, new BytesBasedPublicKeySupplier());
    }

    /**
     * 获取用于密钥生成的算法<br>
     * 获取XXXwithXXX算法的后半部分算法，如果为ECDSA或SM2，返回算法为EC
     * @param algorithm XXXwithXXX算法
     * @return 算法
     */
    public static String getAlgorithmAfterWith(String algorithm) {
        Preconditions.checkNotNull(algorithm, "algorithm must be not null !");
        if(algorithm.contains("SM2") || algorithm.contains("EC")) {
            int indexOfWith = Strings.lastIndexOfIgnoreCase(algorithm, "with");
            if (indexOfWith > 0) {
                algorithm = Strings.substring(algorithm, indexOfWith + "with".length());
            }
        }
        if ("ECDSA".equalsIgnoreCase(algorithm) || "SM2".equalsIgnoreCase(algorithm)) {
            algorithm = "EC";
        }
        return algorithm;
    }

}
