package com.jn.langx.util.random;

import com.jn.langx.security.crypto.key.SecureRandoms;

import java.util.Random;

public class Randoms {
    public static IRandom of(Random random) {
        return new RandomProxy(random);
    }

    public static IRandom ofSecure() {
        return of(SecureRandoms.getDefault());
    }
}
