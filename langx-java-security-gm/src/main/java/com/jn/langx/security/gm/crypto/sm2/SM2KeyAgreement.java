package com.jn.langx.security.gm.crypto.sm2;


import com.jn.langx.security.gm.crypto.sm2.internal.SM2KeyExchangeUtil;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.math.ec.ECPoint;

import javax.crypto.KeyAgreementSpi;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

public class SM2KeyAgreement extends KeyAgreementSpi {
    private ECPrivateKey privateKey;
    private ECPublicKey publicKey;
    private ECPublicKey publicRemote;
    private BigInteger random;
    private byte[] idLocal;
    private byte[] idRemote;
    private boolean active;
    private int keyLength;
    private byte[] shareKey;

    public SM2KeyAgreement() {
        this.privateKey = null;
        this.publicKey = null;
        this.publicRemote = null;
        this.random = null;
        this.idLocal = null;
        this.idRemote = null;
        this.active = false;
        this.keyLength = 0;
        this.shareKey = null;
    }

    @Override
    protected void engineInit(final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void engineInit(final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (!(algorithmParameterSpec instanceof SM2KeyExchangeParameterSepc)) {
            throw new InvalidAlgorithmParameterException("SM2 key agreement requires SM2KeyExchangeParams for init");
        }
        if (!(key instanceof ECPrivateKey)) {
            throw new InvalidKeyException("SM2 key agreement requires ECPrivateKey for initialisation");
        }
        this.privateKey = (ECPrivateKey) key;
        final SM2KeyExchangeParameterSepc sm2KeyExchangeParams = (SM2KeyExchangeParameterSepc) algorithmParameterSpec;
        this.random = sm2KeyExchangeParams.getRandom();
        if (this.random == null) {
            throw new InvalidAlgorithmParameterException("random cannot be null");
        }
        this.publicKey = (ECPublicKey) sm2KeyExchangeParams.getPublicKey();
        if (this.publicKey == null) {
            throw new InvalidAlgorithmParameterException("publicKey cannot be null");
        }
        this.publicRemote = (ECPublicKey) sm2KeyExchangeParams.getPeerPublicKey();
        if (this.publicRemote == null) {
            throw new InvalidAlgorithmParameterException("peerPublicKey cannot be null");
        }
        this.idLocal = sm2KeyExchangeParams.getIdLocal();
        if (this.idLocal == null) {
            throw new InvalidAlgorithmParameterException("idLocal cannot be null");
        }
        this.idRemote = sm2KeyExchangeParams.getIdRemote();
        if (this.idRemote == null) {
            throw new InvalidAlgorithmParameterException("idRemote cannot be null");
        }
        this.keyLength = sm2KeyExchangeParams.getKeyLength();
        this.active = sm2KeyExchangeParams.isActive();
    }

    @Override
    protected Key engineDoPhase(final Key key, final boolean b) throws InvalidKeyException, IllegalStateException {
        ECPoint q = null;
        if (key instanceof ECPublicKey) {
            q = ((ECPublicKey) key).getQ();
        }
        try {
            this.shareKey = SM2KeyExchangeUtil.generateK(this.publicKey, this.privateKey, this.publicRemote, this.random, q, this.idLocal, this.idRemote, this.active, this.keyLength);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return new SecretKeySpec(this.shareKey, "SM2Key");
    }

    @Override
    protected byte[] engineGenerateSecret() throws IllegalStateException {
        return this.shareKey;
    }

    @Override
    protected int engineGenerateSecret(final byte[] array, final int n) throws IllegalStateException, ShortBufferException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected SecretKey engineGenerateSecret(final String s) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
        return new SecretKeySpec(this.shareKey, s);
    }
}
