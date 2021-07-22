package com.jn.langx.security.gm.crypto.sm2;


import com.jn.langx.security.gm.GmJceProvider;
import com.jn.langx.security.gm.crypto.skf.SKF_PrivateKey;
import com.jn.langx.security.gm.crypto.sm2.internal.*;
import com.jn.langx.security.gm.crypto.sm3.internal._SM3DigestImpl;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

public class SM3WithSM2Signature extends BCSignatureSpi {
    private boolean skfEnabled;
    private SKF_PrivateKey skfPri;
    private ECPublicKey publicKey;
    public byte[] id;

    public SM3WithSM2Signature() {
        super(new _SM3DigestImpl(), new SM2Signer(), new StdDSAEncoder());
        this.skfEnabled = false;
        this.skfPri = null;
        this.publicKey = null;
        this.id = null;
    }

    @Override
    protected void engineSetParameter(final AlgorithmParameterSpec algorithmParameterSpec) {
        if (!(algorithmParameterSpec instanceof SM2ParameterSpec)) {
            throw new RuntimeException("SM3WithSM2 must use SM2ParameterSpec");
        }
        final SM2ParameterSpec sm2ParameterSpec = (SM2ParameterSpec) algorithmParameterSpec;
        final PublicKey publicKey = sm2ParameterSpec.getPublicKey();
        try {
            this.publicKey = __SM2Util.toTsgECPublicKey(publicKey);
        } catch (Exception ex) {
            throw new RuntimeException("toTsgECPublicKey");
        }
        this.id = sm2ParameterSpec.getId();
    }

    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        this.skfEnabled = (privateKey instanceof SKF_PrivateKey);
        if (this.skfEnabled) {
            this.skfPri = (SKF_PrivateKey) privateKey;
        } else {
            super.engineInitSign(privateKey);
        }
        try {
            if (this.id == null) {
                this.id = "1234567812345678".getBytes();
            }
            if (this.publicKey == null) {
                final ECPrivateKey ecPrivateKey = (ECPrivateKey) privateKey;
                final BigInteger d = ecPrivateKey.getD();
                final ECParameterSpec parameters = ecPrivateKey.getParameters();
                final ECPoint multiply = parameters.getG().multiply(d);
                this.publicKey = (ECPublicKey) KeyFactory.getInstance("SM2", GmJceProvider.NAME).generatePublic(new ECPublicKeySpec(parameters.getCurve().createPoint(multiply.getX().toBigInteger(), multiply.getY().toBigInteger(), false), parameters));
            }
            __SM2Util.Z(this.id, this.publicKey, this.digest);
        } catch (Exception ex) {
            throw new InvalidKeyException(ex);
        }
    }

    @Override
    protected byte[] engineSign() throws SignatureException {
        if (this.skfEnabled) {
            final byte[] array = new byte[this.digest.getDigestSize()];
            this.digest.doFinal(array, 0);
            try {
                final byte[] doSign = this.skfPri.getCryptoProvider().doSign(array, 0, array.length);
                final byte[] array2 = new byte[32];
                final byte[] array3 = new byte[32];
                System.arraycopy(doSign, 0, array2, 0, 32);
                System.arraycopy(doSign, 32, array3, 0, 32);
                final BigInteger[] array4 = {new BigInteger(1, array2), new BigInteger(1, array3)};
                final byte[] encoded = new DERSequence(new ASN1Integer[]{new ASN1Integer(array4[0]), new ASN1Integer(array4[1])}).getEncoded("DER");
                final byte[] encode = this.encoder.encode(array4[0], array4[1]);
                return encode;
            } catch (Exception ex) {
                throw new SignatureException(ex);
            }
        }
        return super.engineSign();
    }

    @Override
    protected void engineInitVerify(final PublicKey publicKey) throws InvalidKeyException {
        super.engineInitVerify(publicKey);
        try {
            this.publicKey = __SM2Util.toTsgECPublicKey(publicKey);
        } catch (Exception ex) {
            throw new RuntimeException("toTsgECPublicKey");
        }
        if (this.id == null) {
            this.id = "1234567812345678".getBytes();
        }
        __SM2Util.Z(this.id, this.publicKey, this.digest);
    }
}