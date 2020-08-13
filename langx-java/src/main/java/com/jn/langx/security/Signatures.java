package com.jn.langx.security;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

import java.security.*;
import java.security.cert.Certificate;

public class Signatures {
    public static Signature createSignature(@NonNull String algorithm, @Nullable String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
        return Strings.isEmpty(provider) ? Signature.getInstance(algorithm) : Signature.getInstance(algorithm, provider);
    }

    public static Signature createSignature(@NonNull String algorithm, @Nullable String provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        Signature signature = createSignature(algorithm, provider);
        if (secureRandom == null) {
            signature.initSign(privateKey);
        } else {
            signature.initSign(privateKey, secureRandom);
        }
        return signature;
    }

    public static Signature createSignature(@NonNull String algorithm, @Nullable String provider, @NonNull PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        Signature signature = createSignature(algorithm, provider);
        signature.initVerify(publicKey);
        return signature;
    }


    public static Signature createSignature(@NonNull String algorithm, @Nullable String provider, @NonNull Certificate certificate) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        Signature signature = createSignature(algorithm, provider);
        signature.initVerify(certificate);
        return signature;
    }

    public static boolean verify(Signature initedSignaturer, byte[] data, byte[] signature) throws SignatureException{
        Preconditions.checkNotNull(initedSignaturer);
        initedSignaturer.update(data);
        return initedSignaturer.verify(signature);
    }

    public static boolean verify(@NonNull String algorithm, @Nullable String provider,@NonNull PublicKey publicKey, byte[] data, byte[] signature ) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException,SignatureException  {
        return verify(createSignature(algorithm, provider, publicKey), data, signature);
    }

    public static byte[] sign(Signature initedSignaturer, byte[] data) throws SignatureException{
        Preconditions.checkNotNull(initedSignaturer);
        initedSignaturer.update(data);
        return initedSignaturer.sign();
    }

    public static byte[] sign(@NonNull String algorithm, @Nullable String provider,@NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data)throws NoSuchAlgorithmException, NoSuchProviderException,SignatureException,InvalidKeyException {
        return sign(createSignature(algorithm, provider, privateKey, secureRandom),data);
    }
}
