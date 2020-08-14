package com.jn.langx.security;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

import java.security.*;

public class DSAs {

    public static byte[] sign(@NonNull String algorithm, @Nullable String provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data)  {
        return Signatures.sign(algorithm, provider, privateKey, secureRandom, data);
    }

    public static byte[] sign(@NonNull String algorithm, @Nullable Provider provider, @NonNull PrivateKey privateKey, @Nullable SecureRandom secureRandom, @NonNull byte[] data) {
        return Signatures.sign(algorithm, provider.getName(), privateKey, secureRandom, data);
    }
}
