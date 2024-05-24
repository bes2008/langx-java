package com.jn.langx.security.crypto.salt;

import com.jn.langx.util.Strings;

public class StringToBytesGenerator implements BytesSaltGenerator{
    private StringSaltGenerator delegate;

    public StringToBytesGenerator(StringSaltGenerator stringSaltGenerator){
        this.delegate=stringSaltGenerator;
    }

    @Override
    public byte[] get(Integer bytesLength) {
        int charsLength = (bytesLength+1)/2;
        String str=delegate.get(charsLength);
        return Strings.getBytesUtf8(str);
    }
}
