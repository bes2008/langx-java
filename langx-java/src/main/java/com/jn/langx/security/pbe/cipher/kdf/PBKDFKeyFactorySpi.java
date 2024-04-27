package com.jn.langx.security.pbe.cipher.kdf;

import com.jn.langx.Named;
import com.jn.langx.security.SecurityException;
import com.jn.langx.util.Throwables;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class PBKDFKeyFactorySpi extends SecretKeyFactorySpi {
    private PBKDF pbkdf;
    private String algorithm;


    public PBKDFKeyFactorySpi( String algorithm, PBKDF pbkdf){
        this.algorithm =algorithm;
        this.pbkdf = pbkdf;
    }


    @Override
    protected SecretKey engineGenerateSecret(KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof SecretKey){
            return new SecretKeySpec(((SecretKeySpec)keySpec).getEncoded(), algorithm);
        }
        if(keySpec instanceof PBKDFKeySpec){
            PBKDFKeySpec pbeKey=(PBKDFKeySpec)keySpec;
            try {
                DerivedKey derivedKey = derivedKey= pbkdf.transform(
                        new String(pbeKey.getPassword()),
                        pbeKey.getSalt(),
                        pbeKey.getKeyLength(),
                        pbeKey.getIvBitSize(),
                        pbeKey.getIterationCount(),
                        pbeKey.getHashAlgorithm());
                return derivedKey;
            }catch (Throwable e){
                throw new SecurityException(e);
            }
        }
        throw new InvalidKeySpecException("Invalid KeySpec");
    }

    @Override
    protected KeySpec engineGetKeySpec(SecretKey key, Class<?> keySpec) throws InvalidKeySpecException {
        if (keySpec == null)
        {
            throw new InvalidKeySpecException("keySpec parameter is null");
        }
        if (key == null)
        {
            throw new InvalidKeySpecException("key parameter is null");
        }

        if (SecretKeySpec.class.isAssignableFrom(keySpec))
        {
            return new SecretKeySpec(key.getEncoded(), algorithm);
        }
        return null;
    }

    @Override
    protected SecretKey engineTranslateKey(SecretKey key) throws InvalidKeyException {
        if (key == null)
        {
            throw new InvalidKeyException("key parameter is null");
        }

        if (!key.getAlgorithm().equalsIgnoreCase(algorithm))
        {
            throw new InvalidKeyException("Key not of type " + algorithm + ".");
        }

        if(key instanceof DerivedKey){
            try {
                return (SecretKey) ((DerivedKey) key).clone();
            }catch (Throwable e ){
                throw Throwables.wrapAsRuntimeException(e);
            }
        }

        return new SecretKeySpec(key.getEncoded(), algorithm);
    }
}
