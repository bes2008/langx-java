package com.jn.langx.security.gm.crypto.bc.asymmetric.sm2;

import com.jn.langx.util.reflect.Reflects;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jcajce.util.BCJcaJceHelper;
import org.bouncycastle.jcajce.util.JcaJceHelper;

import java.security.*;
import java.security.spec.AlgorithmParameterSpec;

// ref: org.bouncycastle.jcajce.provider.asymmetric.ec.GMSignatureSpi
public class SM2SignatureSpi extends java.security.SignatureSpi {
    private final JcaJceHelper helper = new BCJcaJceHelper();

    private AlgorithmParameters engineParams;
    private AlgorithmParameterSpec paramSpec;

    private final SM2Signer signer;

    SM2SignatureSpi(SM2Signer signer) {
        this.signer = signer;
    }

    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        CipherParameters param = ECUtil.generatePublicKeyParameter(publicKey);
        // 验签时， appRandom 必然是 null
        if (paramSpec != null) {
            byte[] id = extractId();
            if (id != null) {
                param = new ParametersWithID(param, id);
            }
        }

        signer.init(false, param);
    }

    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        CipherParameters param = ECUtil.generatePrivateKeyParameter(privateKey);


        if (appRandom != null) {
            param = new ParametersWithRandom(param, appRandom);
        }

        if (paramSpec != null) {
            byte[] id = extractId();
            if (id != null) {
                param = new ParametersWithID(param, id);
            }
        }
        signer.init(true, param);
    }

    private byte[] extractId() {
        byte[] id = null;
        if (paramSpec != null) {

            if (paramSpec instanceof IDGetter) {
                id = ((IDGetter) paramSpec).getID();
            } else if (Reflects.getFQNClassName(paramSpec.getClass()).equals("org.bouncycastle.jcajce.spec.SM2ParameterSpec")) {
                id = Reflects.invokeAnyMethod(paramSpec, "getID", new Class[0], new Object[0], true, true);
            }

        }
        return id;
    }

    protected void engineUpdate(byte b) throws SignatureException {
        signer.update(b);
    }

    protected void engineUpdate(byte[] bytes, int off, int length) throws SignatureException {
        signer.update(bytes, off, length);
    }

    protected byte[] engineSign() throws SignatureException {
        try {
            return signer.generateSignature();
        } catch (CryptoException e) {
            throw new SignatureException("unable to create signature: " + e.getMessage());
        }
    }

    protected boolean engineVerify(byte[] bytes) throws SignatureException {
        return signer.verifySignature(bytes);
    }

    @Override
    protected void engineSetParameter(AlgorithmParameterSpec params) throws InvalidAlgorithmParameterException {
        if (params instanceof SM2SignParameterSpec) {
            paramSpec = params;
        } else if (params instanceof IDGetter) {
            paramSpec = params;
        } else if (paramSpec != null && Reflects.getFQNClassName(paramSpec.getClass()).equals("org.bouncycastle.jcajce.spec.SM2ParameterSpec")) {
            paramSpec = params;
        } else {
            throw new InvalidAlgorithmParameterException("only SM2ParameterSpec supported");
        }
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (engineParams == null && paramSpec != null) {
            try {
                engineParams = helper.createAlgorithmParameters("PSS");
                engineParams.init(paramSpec);
            } catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
        }

        return engineParams;
    }

    protected void engineSetParameter(String param, Object value) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }

    protected Object engineGetParameter(String param) {
        throw new UnsupportedOperationException("engineGetParameter unsupported");
    }

    public static class SM3WithSM2 extends SM2SignatureSpi {
        public SM3WithSM2() {
            super(new SM2Signer());
        }
    }

    public class Sha256WithSM2 extends SM2SignatureSpi{
        public Sha256WithSM2()
        {
            super(new SM2Signer(new SHA256Digest()));
        }
    }
}