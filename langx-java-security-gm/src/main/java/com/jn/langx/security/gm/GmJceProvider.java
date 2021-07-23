package com.jn.langx.security.gm;

import com.jn.langx.security.gm.crypto.SM2EcbBlockCipher;
import com.jn.langx.security.gm.crypto.SM2KeyPairGeneratorSpi;
import com.jn.langx.util.reflect.Reflects;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;

public class GmJceProvider extends Provider {
    private static final long serialVersionUID = 1443132961964116159L;
    private static final String INFO = "GM JCE provider";
    public static final String NAME = "langx-security-gm-provider";


    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public GmJceProvider() {
        super(NAME, 1.0, INFO);

        this.put("Cipher.SM2", Reflects.getFQNClassName(SM2EcbBlockCipher.class));
        //    this.put("KeyAgreement.SM2", Reflects.getFQNClassName(SM2KeyAgreement.class));
        //    this.put("Signature.1.2.156.10197.1.501", Reflects.getFQNClassName(SM3WithSM2Signature.class));
        //    this.put("Signature.SM3WithSM2", Reflects.getFQNClassName(SM3WithSM2Signature.class));
        //    this.put("Signature.NoneWithSM2", Reflects.getFQNClassName(NoneWithSM2Signature.class));
        this.put("KeyFactory.ECDSA", Reflects.getFQNClassName(KeyFactorySpi.ECDSA.class));
        this.put("KeyFactory.EC", Reflects.getFQNClassName(KeyFactorySpi.EC.class));
        this.put("Alg.Alias.KeyFactory.SM2", "EC");
        this.put("Alg.Alias.KeyFactory.1.2.840.10045.2.1", "EC");
        this.put("KeyPairGenerator.SM2", Reflects.getFQNClassName(SM2KeyPairGeneratorSpi.class));

        //    this.put("KeyPairGenerator.ECDSA", Reflects.getFQNClassName(KeyPairGeneratorSpi.ECDSA.class));
    /*
        this.put("CertificateFactory.X.509", Reflects.getFQNClassName(CertificateFactory.class));
        this.put("Alg.Alias.CertificateFactory.X509", "X.509");
        this.put("KeyStore.JKS", "sun.security.provider.JavaKeyStore$JKS");
        this.put("KeyStore.PKCS12", Reflects.getFQNClassName(JDKPKCS12KeyStore.BCPKCS12KeyStore.class));
        this.put("CertPathValidator.PKIX", Reflects.getFQNClassName(PKIXCertPathValidatorSpi.class));

        this.put("MessageDigest.SM3", Reflects.getFQNClassName(SM3MessageDigest.class));
        this.put("Mac.HmacSM3", Reflects.getFQNClassName(HMacWithSM3.class));

        this.put("Cipher.SM4", Reflects.getFQNClassName(SM4EcbBlockCipher.class));
        this.put("Cipher.SM4/CBC", Reflects.getFQNClassName(SM4CbcBlockCipher.class));
        this.put("AlgorithmParameters.SM4", Reflects.getFQNClassName(SM4AlgorithmParameters.class));
        this.put("AlgorithmParameterGenerator.SM4", Reflects.getFQNClassName(SM4AlgorithmParameterGeneratorSpi.class));
        this.put("KeyGenerator.SM4", Reflects.getFQNClassName(SM4KeyGeneratorSpi.class));
    */
    }
}