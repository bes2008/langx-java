package com.jn.langx.security.crypto.salt;

import com.jn.langx.util.id.Nanoids;
import com.jn.langx.util.random.Randoms;

public class RandomStringSaltGenerator implements StringSaltGenerator {
    private String alphabet = Nanoids.DEFAULT_ALPHABET;

    public RandomStringSaltGenerator() {
    }

    public RandomStringSaltGenerator(String alphabet) {
        this.alphabet = alphabet;
    }

    @Override
    public String get(Integer charsLength) {
        return Randoms.randomString(alphabet, charsLength);
    }
}
