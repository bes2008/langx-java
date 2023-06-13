package com.jn.langx.test.codec;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

public class Base64Tests {
    @Test
    public void test(){
        String b64 = Base64.encodeBase64String("1".getBytes(Charsets.UTF_8));
        System.out.println(b64);
        String raw = Base64.decodeBase64ToString(b64);
        System.out.println(raw);
    }
}
