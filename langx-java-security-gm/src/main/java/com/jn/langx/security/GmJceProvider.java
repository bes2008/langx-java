package com.jn.langx.security;

import com.jn.langx.security.gm.crypto.sm2.Sm2Cipher;
import com.jn.langx.security.gm.crypto.sm2.SM2KeyPairGenerator;
import com.jn.langx.security.gm.crypto.sm2.impl.NoneWithSM2;
import com.jn.langx.security.gm.crypto.sm2.impl.SM2KeyAgreement;
import com.jn.langx.security.gm.crypto.sm2.impl.SM3WithSM2;
import com.jn.langx.security.gm.crypto.sm3.HMacSM3;
import com.jn.langx.security.gm.crypto.sm3.SM3Jce;
import com.jn.langx.security.gm.crypto.sm4.SM4CbcBlockCipher;
import com.jn.langx.security.gm.crypto.sm4.SM4CtrBlockCipher;
import com.jn.langx.security.gm.crypto.sm4.SM4EcbBlockCipher;
import com.jn.langx.util.reflect.Reflects;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi;
import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JDKPKCS12KeyStore;
import org.bouncycastle.jce.provider.PKIXCertPathValidatorSpi;

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
        this.put("Cipher.SM4", Reflects.getFQNClassName(SM4EcbBlockCipher.class));
        this.put("Cipher.SM4/CBC", Reflects.getFQNClassName(SM4CbcBlockCipher.class));
        this.put("Cipher.SM4/CTR", Reflects.getFQNClassName(SM4CtrBlockCipher.class));
        this.put("Cipher.SM2", Reflects.getFQNClassName(Sm2Cipher.class));
        this.put("MessageDigest.SM3", Reflects.getFQNClassName(SM3Jce.class));
        this.put("Mac.HmacSM3", Reflects.getFQNClassName(HMacSM3.class));
        this.put("KeyAgreement.SM2", Reflects.getFQNClassName(SM2KeyAgreement.class));
        this.put("Signature.1.2.156.10197.1.501", Reflects.getFQNClassName(SM3WithSM2.class));
        this.put("Signature.SM3WithSM2", Reflects.getFQNClassName(SM3WithSM2.class));
        this.put("Signature.NoneWithSM2", Reflects.getFQNClassName(NoneWithSM2.class));
        this.put("CertificateFactory.X.509", Reflects.getFQNClassName(CertificateFactory.class));
        this.put("Alg.Alias.CertificateFactory.X509", "X.509");
        this.put("KeyFactory.ECDSA", Reflects.getFQNClassName(KeyFactorySpi.ECDSA.class));
        this.put("KeyFactory.EC", Reflects.getFQNClassName(KeyFactorySpi.EC.class));
        this.put("Alg.Alias.KeyFactory.SM2", "EC");
        this.put("Alg.Alias.KeyFactory.1.2.840.10045.2.1", "EC");
        this.put("KeyPairGenerator.SM2", Reflects.getFQNClassName(SM2KeyPairGenerator.class));
        this.put("KeyPairGenerator.ECDSA", Reflects.getFQNClassName(KeyPairGeneratorSpi.ECDSA.class));
        this.put("KeyStore.JKS", "sun.security.provider.JavaKeyStore$JKS");
        this.put("KeyStore.PKCS12", Reflects.getFQNClassName(JDKPKCS12KeyStore.BCPKCS12KeyStore.class));
        this.put("CertPathValidator.PKIX", Reflects.getFQNClassName(PKIXCertPathValidatorSpi.class));
    }
}