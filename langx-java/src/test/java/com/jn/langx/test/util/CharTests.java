package com.jn.langx.test.util;

import com.jn.langx.util.io.bytes.Utf8s;
import org.junit.Test;

public class CharTests {
    @Test
    public void test() {
        System.out.println(Utf8s.hexToChar("\\x5c"));
        System.out.println(Utf8s.hexToChar("\\xB5"));
        System.out.println(Utf8s.hexToChar("\\uD842"));
        System.out.println(Utf8s.hexToChar("\\uDFB7"));
        System.out.println(Utf8s.hexToChar("\\u20BB"));
        System.out.println(Utf8s.hexToChar("\\u1000"));
        System.out.println(Utf8s.hexToChar("\\x5C"));
    }
}
