package com.jn.langx.security.gm.crypto.sm2;


import com.jn.langx.security.gm.util._utils;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.EC5Util;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi;
import org.bouncycastle.jcajce.provider.config.ProviderConfiguration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @see // cn.gmssl.crypto.SM2KeyPairGenerator
 */
public class SM2KeyPairGenerator extends KeyPairGeneratorSpi
{
    private ECKeyPairGenerator engine;
    private String algorithm;
    private boolean initialised;
    private int strength;
    private AlgorithmParameterSpec params;
    private ProviderConfiguration configuration;

    public SM2KeyPairGenerator() {
        super("SM2");
        this.engine = new ECKeyPairGenerator();
        this.algorithm = null;
        this.initialised = false;
        this.strength = 256;
        this.params = null;
        this.algorithm = "SM2";
        this.configuration = BouncyCastleProvider.CONFIGURATION;
    }

    @Override
    public void initialize(final int strength, final SecureRandom secureRandom) {
        this.strength = strength;
        if (strength == 256) {
            try {
                this.initialize(_utils.getSM2NamedCuve(), secureRandom);
                return;
            }
            catch (InvalidAlgorithmParameterException ex) {
                throw new InvalidParameterException("key size not configurable.");
            }
        }
        throw new InvalidParameterException("unknown key size.");
    }

    @Override
    public void initialize(final AlgorithmParameterSpec params, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        this.params = params;
        if (params instanceof ECParameterSpec) {
            final ECParameterSpec ecParameterSpec = (ECParameterSpec)params;
            this.engine.init(new ECKeyGenerationParameters(new ECDomainParameters(ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN()), secureRandom));
            this.initialised = true;
        }
        else {
            if (!(params instanceof java.security.spec.ECParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameter object not a ECParameterSpec");
            }
            final java.security.spec.ECParameterSpec ecParameterSpec2 = (java.security.spec.ECParameterSpec)params;
            final ECCurve convertCurve = EC5Util.convertCurve(ecParameterSpec2.getCurve());
            this.engine.init(new ECKeyGenerationParameters(new ECDomainParameters(convertCurve, EC5Util.convertPoint(convertCurve, ecParameterSpec2.getGenerator(), false), ecParameterSpec2.getOrder(), BigInteger.valueOf(ecParameterSpec2.getCofactor())), secureRandom));
            this.initialised = true;
        }
    }

    @Override
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            this.initialize(this.strength, new SecureRandom());
        }
        final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
        final ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters)generateKeyPair.getPublic();
        final ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters)generateKeyPair.getPrivate();
        if (this.params instanceof ECParameterSpec) {
            final ECParameterSpec ecParameterSpec = (ECParameterSpec)this.params;
            final BCECPublicKey bcecPublicKey = new BCECPublicKey(this.algorithm, ecPublicKeyParameters, ecParameterSpec, this.configuration);
            return new KeyPair(bcecPublicKey, new BCECPrivateKey(this.algorithm, ecPrivateKeyParameters, bcecPublicKey, ecParameterSpec, this.configuration));
        }
        final java.security.spec.ECParameterSpec ecParameterSpec2 = (java.security.spec.ECParameterSpec)this.params;
        final BCECPublicKey bcecPublicKey2 = new BCECPublicKey(this.algorithm, ecPublicKeyParameters, ecParameterSpec2, this.configuration);
        return new KeyPair(bcecPublicKey2, new BCECPrivateKey(this.algorithm, ecPrivateKeyParameters, bcecPublicKey2, ecParameterSpec2, this.configuration));
    }
}
