package com.jn.langx.test.codec;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

public class Base64Tests {
    @Test
    public void test(){
        System.out.println(Base64.encodeBase64String("1".getBytes(Charsets.UTF_8)));
        System.out.println(Base64.encodeBase64ToString("1".getBytes(Charsets.UTF_8)));
    }
}
