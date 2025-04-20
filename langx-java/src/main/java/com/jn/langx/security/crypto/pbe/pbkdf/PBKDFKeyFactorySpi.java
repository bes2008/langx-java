package com.jn.langx.security.crypto.pbe.pbkdf;

import com.jn.langx.security.SecurityException;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.reflect.Reflects;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * @since 5.3.9
 */
public class PBKDFKeyFactorySpi extends SecretKeyFactorySpi {
    private PBKDFEngine pbkdf;
    private String pbeAlgorithm;

    public PBKDFKeyFactorySpi(String pbeAlgorithm, PBKDFEngine pbkdf) {
        this.pbeAlgorithm = pbeAlgorithm;
        this.pbkdf = pbkdf;
    }


    @Override
    protected SecretKey engineGenerateSecret(KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof SecretKey) {
            return new SecretKeySpec(((SecretKeySpec) keySpec).getEncoded(), pbeAlgorithm);
        }
        if (keySpec instanceof PBKDFKeySpec) {
            PBKDFKeySpec pbeKeySpec = (PBKDFKeySpec) keySpec;
            try {
                DerivedPBEKey derivedKey = pbkdf.apply(pbeAlgorithm, pbeKeySpec);
                return derivedKey;
            } catch (Throwable e) {
                throw new SecurityException(e);
            }
        }
        throw new InvalidKeySpecException("Invalid KeySpec");
    }

    @Override
    protected KeySpec engineGetKeySpec(SecretKey key, Class<?> keySpec) throws InvalidKeySpecException {
        if (keySpec == null) {
            throw new InvalidKeySpecException("keySpec parameter is null");
        }
        if (key == null) {
            throw new InvalidKeySpecException("key parameter is null");
        }

        if (Reflects.isSubClassOrEquals(DerivedPBEKey.class, keySpec)) {
            DerivedPBEKey pbeKey = (DerivedPBEKey) key;
            return new PBKDFKeySpec(pbeKey.getPassword(), pbeKey.getSalt(), pbeKey.getKeyBitSize(), pbeKey.getIVBitSize(), pbeKey.getIterationCount(), pbeKey.getHashAlgorithm());
        }

        if (SecretKeySpec.class.isAssignableFrom(keySpec)) {
            return new SecretKeySpec(key.getEncoded(), this.pbeAlgorithm);
        }
        return null;
    }

    @Override
    protected SecretKey engineTranslateKey(SecretKey key) throws InvalidKeyException {
        if (key == null) {
            throw new InvalidKeyException("key parameter is null");
        }

        if (!key.getAlgorithm().equalsIgnoreCase(this.pbeAlgorithm)) {
            throw new InvalidKeyException("Key not of type " + this.pbeAlgorithm + ".");
        }

        if (key instanceof DerivedPBEKey) {
            try {
                return (SecretKey) ((DerivedPBEKey) key).clone();
            } catch (Throwable e) {
                throw Throwables.wrapAsRuntimeException(e);
            }
        }

        return new SecretKeySpec(key.getEncoded(), this.pbeAlgorithm);
    }
}
