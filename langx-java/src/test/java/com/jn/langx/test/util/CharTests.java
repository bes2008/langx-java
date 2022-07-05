package com.jn.langx.test.util;

import com.jn.langx.util.Chars;
import org.junit.Test;

public class CharTests {
    @Test
    public void test(){
        System.out.println(Chars.fromHex("\\x5c"));
        System.out.println(Chars.fromHex("\\xB5"));
        System.out.println(Chars.fromHex("\\uD842"));
        System.out.println(Chars.fromHex("\\uDFB7"));
        System.out.println(Chars.fromHex("\\u20BB7"));
        System.out.println(Chars.fromHex("\\u1000"));
    }
}
