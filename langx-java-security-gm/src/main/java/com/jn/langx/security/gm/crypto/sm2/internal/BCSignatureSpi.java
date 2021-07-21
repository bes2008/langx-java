package com.jn.langx.security.gm.crypto.sm2.internal;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.ECUtil;
import org.bouncycastle.jcajce.provider.asymmetric.util.DSABase;
import org.bouncycastle.jcajce.provider.asymmetric.util.DSAEncoder;
import org.bouncycastle.jce.interfaces.ECKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;

public class BCSignatureSpi extends DSABase {
    public BCSignatureSpi(final Digest digest, final DSA dsa, final DSAEncoder dsaEncoder) {
        super(digest, dsa, dsaEncoder);
    }

    protected void engineInitVerify(PublicKey publicKey)
            throws InvalidKeyException {
        CipherParameters param;

        if (publicKey instanceof ECPublicKey) {
            param = ECUtil.generatePublicKeyParameter(publicKey);
        } else {
            try {
                byte[] bytes = publicKey.getEncoded();

                publicKey = BouncyCastleProvider.getPublicKey(SubjectPublicKeyInfo.getInstance(bytes));

                if (publicKey instanceof ECPublicKey) {
                    param = ECUtil.generatePublicKeyParameter(publicKey);
                } else {
                    throw new InvalidKeyException("can't recognise key type in ECDSA based signer");
                }
            } catch (Exception e) {
                throw new InvalidKeyException("can't recognise key type in ECDSA based signer");
            }
        }

        digest.reset();
        signer.init(false, param);
    }

    protected void engineInitSign(
            PrivateKey privateKey)
            throws InvalidKeyException {
        CipherParameters param;

        if (privateKey instanceof ECKey) {
            param = ECUtil.generatePrivateKeyParameter(privateKey);
        } else {
            throw new InvalidKeyException("can't recognise key type in ECDSA based signer");
        }

        digest.reset();

        if (appRandom != null) {
            signer.init(true, new ParametersWithRandom(param, appRandom));
        } else {
            signer.init(true, param);
        }
    }

}
