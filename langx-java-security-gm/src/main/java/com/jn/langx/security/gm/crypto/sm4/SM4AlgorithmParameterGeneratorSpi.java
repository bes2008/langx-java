package com.jn.langx.security.gm.crypto.sm4;

import com.jn.langx.util.Throwables;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameterGenerator;

import javax.crypto.spec.IvParameterSpec;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

public class SM4AlgorithmParameterGeneratorSpi extends BaseAlgorithmParameterGenerator {
    protected void engineInit(
            AlgorithmParameterSpec genParamSpec,
            SecureRandom random)
            throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for SM4 parameter generation.");
    }

    protected AlgorithmParameters engineGenerateParameters() {
        byte[] iv = new byte[16];

        if (random == null) {
            try {
                random = SecureRandom.getInstance("SHA1PRNG");
            }catch (NoSuchAlgorithmException ex){
                throw Throwables.wrapAsRuntimeException(ex);
            }
        }

        random.nextBytes(iv);

        AlgorithmParameters params;

        try {
            params = AlgorithmParameters.getInstance("SM4");
            params.init(new IvParameterSpec(iv));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return params;
    }
}
