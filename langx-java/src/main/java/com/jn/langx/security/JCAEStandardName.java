package com.jn.langx.security;

import java.security.*;

/**
 * <p>
 * 这个类是一个工具类，在使用JCA,JCE的API时可以使用到的。
 * 使用JCA,JCE的API时，由于这部分的设计，在获取各种各样的的engine class的实例时，
 * 例如<code>MessageDigest.getInstance(String)</code>。<br>
 * 因为名称比较固定，但容易出错，这个类就是方便名称的使用的。</p>
 * 使用时只需要：<code>MessageDigest.getInstance(JCAEngineInstanceName.MD5.getName());</code>
 * <p>
 * <p>
 * Name目前有3种：Algorithm名称、Certificate名称、KeyStore名称、Service Attribute名称<br>
 * <strong>Algorithm:</strong><br>
 * 加密算法可以分为3大类：单向加密算法（也就是摘要算法）、对称加密算法、非对称加密算法。<br>
 * 其中单向加密算法是不需要进行解密的，所以在使用单向加密算法时，不需要使用key等API。<br>
 * 对称加密算法和非对称加密算法都需要进行发送者加密、接收者解密的过程，所以会使用Key相关的API。<br/>
 * 对称加密算法使用的是SecretKey，非对称加密则使用的是PublicKey、PrivateKey。<br/>
 * 算法名称类型可以分为：MessageDigest、Key、 Signature、 Random ，可以参考注解@Algorithm 。<br>
 * MessageDigest算法、Key and Parameter算法的名称都比较简单，这里就不一一说明了。<br>
 * Signature算法的name的命名规则：<br>
 * 1) &lt;digest&gt;with&lt;encryption&gt;:这种形式是MessageDigest算法与keyPair算法的结合;<br>
 * 2) &lt;digest&gt;with&lt;encryption&gt;and&lt;mgf&gt; mgf 是mark generation function，即掩码生成函数。
 * <br>
 * Signature算法相关的实例的名称，例如SHA1withDSA，其实就是keyPair算法+MessageDigest算法的结合。
 * 这是因为Signature本来就是在PrivateKey的基础上对数据使用了MessageDigest算法，从而生成Signature。</p><br>
 * <p>
 * <p><strong>Certificate:</strong><br>
 * 著名的有X.509
 * </p>
 * <p><strong>KeyStore:</strong><br>
 * JKS、PKCS12
 * </p><p>
 * <strong>Service Attribute:</strong><br>
 * JKS、PKCS12
 * </p>
 * <p>
 * <p>
 * JCE中的名称太多了，也没有什么规律，这里就不指明了。可以参考：<a href="http://docs.oracle.com/javase/1.5.0/docs/guide/security/CryptoSpec.html#AppB">JCE 实例名称说明</a>
 *
 * @author fs1194361820@163.com
 */
public enum JCAEStandardName {
    /********************Perso Random Number Generate *******/
    @Algorithm(name="NativePRNG", apply = SecureRandom.class)
    NativePRNG,
    @Algorithm(name="NativePRNGBlocking", apply = SecureRandom.class)
    NativePRNGBlocking,
    @Algorithm(name="NativePRNGNonBlocking", apply = SecureRandom.class)
    NativePRNGNonBlocking,
    @Algorithm(name="PKCS11", apply = SecureRandom.class)
    PKCS11PRNG,
    @Algorithm(name="DRBG", apply = SecureRandom.class)
    DRBG,
    @Algorithm(name="SHA1PRNG", apply = SecureRandom.class)
    SHA1PRNG,
    @Algorithm(name="Windows-PRNG", apply = SecureRandom.class)
    Windows_PRNG,

    /*********************MessageDigest**********************/
    @Algorithm(name = "SHA-1", apply = MessageDigest.class)
    SHA_1,
    @Algorithm(name = "SHA-256", apply = MessageDigest.class)
    SHA_256,
    @Algorithm(name = "SHA-384", apply = MessageDigest.class)
    SHA_384,
    @Algorithm(name = "SHA-512", apply = MessageDigest.class)
    SHA_512,
    @Algorithm(name = "MD2", apply = MessageDigest.class)
    MD2,
    @Algorithm(name = "MD5", apply = MessageDigest.class)
    MD5,

    /*********************KeyPair, Key Parameter*************/
    @Algorithm(name = "DSA", apply = {KeyPairGenerator.class, AlgorithmParameterGenerator.class})
    DSA,
    @Algorithm(name = "RSA", apply = KeyPairGenerator.class)
    RSA,

    /********************Digital Signature***************************/
    @Algorithm(name = "NONEwithRSA", apply = Signature.class)
    NONE_DSA,
    @Algorithm(name = "MD2withRSA", apply = Signature.class)
    MD2_RSA,
    @Algorithm(name = "MD5withRSA", apply = Signature.class)
    MD5_RSA,
    @Algorithm(name = "SHA1withRSA", apply = Signature.class)
    SHA1_RSA,
    @Algorithm(name = "SHA224withRSA", apply = Signature.class)
    SHA224_RSA,
    @Algorithm(name = "SHA256withRSA", apply = Signature.class)
    SHA256_RSA,
    @Algorithm(name = "SHA384withRSA", apply = Signature.class)
    SHA384_RSA,
    @Algorithm(name = "SHA512withRSA", apply = Signature.class)
    SHA512_RSA,
    @Algorithm(name = "SHA512/224withRSA", apply = Signature.class)
    SHA512_224_RSA,
    @Algorithm(name = "SHA512/256withRSA", apply = Signature.class)
    SHA512_256_RSA,
    @Algorithm(name = "SHA3-224withRSA", apply = Signature.class)
    SHA3_224_RSA,
    @Algorithm(name = "SHA3-256withRSA", apply = Signature.class)
    SHA3_256_RSA,
    @Algorithm(name = "SHA3-384withRSA", apply = Signature.class)
    SHA3_384_RSA,
    @Algorithm(name = "SHA3-512withRSA", apply = Signature.class)
    SHA3_512_RSA,
    @Algorithm(name = "SHA1withDSA", apply = Signature.class)
    SHA1_DSA,
    ECDSA,

    /************************Certificate*****************************/
    X509("X.509"),


    /********************KeyStore types***************************/
    // https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#Key
    JCEKS,
    JKS,
    DKS,
    PKCS11,
    PKCS12;

    JCAEStandardName() {
        Algorithm algorithm = null;
        try {
            algorithm = JCAEStandardName.class.getDeclaredField(name()).getAnnotation(Algorithm.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if (algorithm != null) {
            this.name = algorithm.name();
        } else {
            this.name = name();
        }
    }

    JCAEStandardName(String name) {
        this.name = name;
    }

    //	Override the Enum#name
    private String name;

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
