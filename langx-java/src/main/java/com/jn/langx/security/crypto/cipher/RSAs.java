package com.jn.langx.security.crypto.cipher;

import java.security.Provider;
import java.security.SecureRandom;

/**
 * 公钥都是 X509的。
 * <p>
 * 在pkcs1 规范中，定义了RSA的格式，规范等。
 * 在pkcs8 规范中，描述私有密钥信息格式，该信息包括公开密钥算法的私有密钥以及可选的属性集等。也就是说，针对RSA 算法而言，多种非对称加密算法都可以使用 pkcs8 格式。
 * pkcs 8 比pkcs 1中的内容更丰富一些。
 * 但两种可以相互转换的。
 */
public class RSAs extends Asymmetrics {

    public static byte[] encrypt(byte[] bytes, byte[] pubKey) {
        return encrypt(bytes, pubKey, null, null, null);
    }

    public static byte[] encrypt(byte[] bytes, byte[] pubKey, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        return encrypt(bytes, pubKey, "RSA", algorithmTransformation, provider, secureRandom);
    }

    public static byte[] decrypt(byte[] bytes, byte[] priKey) {
        return decrypt(bytes, priKey, null, null, null);
    }

    public static byte[] decrypt(byte[] bytes, byte[] priKey, String algorithmTransformation, Provider provider, SecureRandom secureRandom) {
        return decrypt(bytes, priKey, "RSA", algorithmTransformation, provider, secureRandom);
    }


    /**
     * 规范链接：https://blog.csdn.net/qq_39385118/article/details/107510032
     * PKCS#1 RSA Public Key 格式：
     * <pre>
     *
     *     RSAPublicKey ::= SEQUENCE {
     *         modulus           INTEGER,  -- n
     *         publicExponent    INTEGER   -- e
     *     }
     * </pre>
     * <p>
     * PKCS#1 RSA Private Key 格式：
     * <pre>
     * RSAPrivateKey ::= SEQUENCE {
     *   version           Version,
     *   modulus           INTEGER,  -- n
     *   publicExponent    INTEGER,  -- e
     *   privateExponent   INTEGER,  -- d
     *   prime1            INTEGER,  -- p
     *   prime2            INTEGER,  -- q
     *   exponent1         INTEGER,  -- d mod (p-1)
     *   exponent2         INTEGER,  -- d mod (q-1)
     *   coefficient       INTEGER,  -- (inverse of q) mod p
     *   otherPrimeInfos   OtherPrimeInfos OPTIONAL
     * }
     * </pre>
     * <p>
     * PKCS#8 Private Key 格式：
     * <pre>
     * PrivateKeyInfo ::= SEQUENCE {
     *   version         Version,
     *   algorithm       AlgorithmIdentifier,
     *   PrivateKey      BIT STRING
     * }
     *
     * AlgorithmIdentifier ::= SEQUENCE {
     *   algorithm       OBJECT IDENTIFIER,
     *   parameters      ANY DEFINED BY algorithm OPTIONAL
     * }
     *
     * </pre>
     * <p>
     * <p>
     * PKCS#8 加密的 Private Key 格式：
     * <pre>
     * EncryptedPrivateKeyInfo ::= SEQUENCE {
     *   encryptionAlgorithm  EncryptionAlgorithmIdentifier,
     *   encryptedData        EncryptedData
     * }
     *
     * EncryptionAlgorithmIdentifier ::= AlgorithmIdentifier
     * EncryptedData ::= OCTET STRING
     *  </pre>
     * <p>
     * // 在 pkcs1_bytes 前面 放26个 byte，就变成了 pkcs8_bytes， 这 26个字节，就是他俩结构的差异
     *
     * @param pkcs1PrivateKey pkcs1 格式的 private key, base64 编码后的
     * @return pkcs8 格式的 private key，返回的是 未经过 base64 编码的
     */
    static byte[] pkcs1_to_pkcs8(byte[] pkcs1PrivateKey) {

        return null;
    }

    /**
     * @param pkcs8PrivateKey 符合pkcs#8 标准的 der 格式的 bytes，并且是 未加密的
     * @return pkcs#1 标准的 der格式的bytes
     */
    public static byte[] pkcs8_to_pkcs1(byte[] pkcs8PrivateKey) {
        int pkcs1_bytes_length = pkcs8PrivateKey.length - 26;
        byte[] pkcs1_bytes = new byte[pkcs1_bytes_length];
        System.arraycopy(pkcs8PrivateKey, 26, pkcs1_bytes, 0, pkcs1_bytes_length);
        return pkcs1_bytes;
    }

}
