package com.jn.langx.security.gm.bc.crypto.asymmetric.sm2;

import com.jn.langx.security.crypto.cipher.Asymmetrics;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedPublicKeySupplier;
import com.jn.langx.util.Preconditions;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

class _sm2 {
    /**
     * 构建ECDomainParameters对象
     *
     * @param parameterSpec ECParameterSpec
     * @return {@link ECDomainParameters}
     */
    public static ECDomainParameters toECDomainParams(ECParameterSpec parameterSpec) {
        return new ECDomainParameters(
                parameterSpec.getCurve(),
                parameterSpec.getG(),
                parameterSpec.getN(),
                parameterSpec.getH());
    }

    /**
     * 构建ECDomainParameters对象
     *
     * @param curveName Curve名称
     * @return {@link ECDomainParameters}
     */
    public static ECDomainParameters toECDomainParams(String curveName) {
        return toECDomainParams(ECUtil.getNamedCurveByName(curveName));
    }

    /**
     * 构建ECDomainParameters对象
     *
     * @param x9ECParameters {@link X9ECParameters}
     * @return {@link ECDomainParameters}
     */
    public static ECDomainParameters toECDomainParams(X9ECParameters x9ECParameters) {
        return new ECDomainParameters(
                x9ECParameters.getCurve(),
                x9ECParameters.getG(),
                x9ECParameters.getN(),
                x9ECParameters.getH()
        );
    }

    /**
     * 私钥转换为 {@link ECPrivateKeyParameters}
     *
     * @param key key
     * @return
     * @throws InvalidKeyException
     */
    public static ECPrivateKeyParameters privateKeyToParams(String algorithm, byte[] key) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException {
        Preconditions.checkNotNull(key, "key must be not null !");
        PrivateKey privateKey = generatePrivateKey(algorithm, key);
        return (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(privateKey);
    }

    /**
     * 生成私钥
     *
     * @param algorithm 算法
     * @param key       key
     * @return
     */
    public static PrivateKey generatePrivateKey(String algorithm, byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Preconditions.checkNotNull(algorithm, "algorithm must be not null !");
        Preconditions.checkNotNull(key, "key must be not null !");
        KeySpec keySpec = new PKCS8EncodedKeySpec(key);
        algorithm = Asymmetrics.getAlgorithmAfterWith(algorithm);
        return getKeyFactory(algorithm).generatePrivate(keySpec);
    }

    /**
     * 公钥转换为 {@link ECPublicKeyParameters}
     *
     * @param key key
     * @return
     * @throws InvalidKeyException
     */
    public static ECPublicKeyParameters publicKeyToParams(String algorithm, byte[] key) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException {
        Preconditions.checkNotNull(key, "key must be not null !");
        PublicKey publicKey = new BytesBasedPublicKeySupplier().get(key, algorithm, null);
        return (ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(publicKey);
    }

    /**
     * 获取{@link KeyFactory}
     *
     * @param algorithm 非对称加密算法
     * @return {@link KeyFactory}
     */
    private static KeyFactory getKeyFactory(String algorithm) throws NoSuchAlgorithmException {
        final Provider provider = new BouncyCastleProvider();
        return KeyFactory.getInstance(algorithm, provider);
    }

    /**
     * 加密
     *
     * @param data      数据
     * @param publicKey 公钥
     * @return 加密之后的数据
     */
    public static byte[] encrypt(byte[] data, byte[] publicKey) throws Exception {
        CipherParameters pubKeyParameters = new ParametersWithRandom(publicKeyToParams("SM2", publicKey));
        SM2Engine engine = new SM2Engine();
        engine.init(true, pubKeyParameters);
        return engine.processBlock(data, 0, data.length);
    }

    /**
     * 解密
     *
     * @param data       数据
     * @param privateKey 私钥
     * @return 解密之后的数据
     */
    public static byte[] decrypt(byte[] data, byte[] privateKey) throws Exception {
        CipherParameters privateKeyParameters = privateKeyToParams("SM2", privateKey);
        SM2Engine engine = new SM2Engine();
        engine.init(false, privateKeyParameters);
        byte[] byteDate = engine.processBlock(data, 0, data.length);
        return byteDate;
    }

    /**
     * 签名
     *
     * @param data 数据
     * @return 签名
     */
    public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
        SM2Signer signer = new SM2Signer();
        CipherParameters param = new ParametersWithRandom(privateKeyToParams("SM2", privateKey));
        signer.init(true, param);
        signer.update(data, 0, data.length);
        return signer.generateSignature();
    }

    /**
     * 用公钥检验数字签名的合法性
     *
     * @param data      数据
     * @param sign      签名
     * @param publicKey 公钥
     * @return 是否验证通过
     */
    public static boolean verify(byte[] data, byte[] sign, byte[] publicKey) throws Exception {
        SM2Signer signer = new SM2Signer();
        CipherParameters param = publicKeyToParams("SM2", publicKey);
        signer.init(false, param);
        signer.update(data, 0, data.length);
        return signer.verifySignature(sign);
    }

}
