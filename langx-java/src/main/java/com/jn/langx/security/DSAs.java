package com.jn.langx.security;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;

public class DSAs {

    public static byte[] sign( @Nullable String provider, byte[] privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data){
        PrivateKey privKey = PKIs.createPrivateKey("DSA", provider, new PKCS8EncodedKeySpec(privateKey));
        return sign(provider, privKey, secureRandom, data);
    }

    public static byte[] sign(@Nullable String provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data)  {
        return Signatures.sign("DSA", provider, privateKey, secureRandom, data);
    }

    public static byte[] sign(@Nullable Provider provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data) {
        return Signatures.sign("DSA", provider.getName(), privateKey, secureRandom, data);
    }
}
