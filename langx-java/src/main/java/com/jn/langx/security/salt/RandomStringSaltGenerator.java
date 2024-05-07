package com.jn.langx.security.salt;

import com.jn.langx.util.Strings;
import com.jn.langx.util.id.Nanoids;
import com.jn.langx.util.random.Randoms;

public class RandomStringSaltGenerator implements BytesSaltGenerator {
    private String alphabet = Nanoids.DEFAULT_ALPHABET;
    public RandomStringSaltGenerator(){
    }

    public RandomStringSaltGenerator(String alphabet){
        this.alphabet=alphabet;
    }

    @Override
    public byte[] get(Integer bytesLength) {
        String str= Randoms.randomString(alphabet,(bytesLength+1)/2);
        return Strings.getBytesUtf8(str);
    }
}
