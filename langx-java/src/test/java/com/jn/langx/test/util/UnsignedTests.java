package com.jn.langx.test.util;

import com.jn.langx.util.Unsigneds;
import com.jn.langx.util.io.Charsets;
import org.junit.Assert;
import org.junit.Test;

public class UnsignedTests {

    @Test
    public void testUnsignedByte() {

        byte[] bytes = {-128, -20, -1, 0, 20, 127, 24, 45, 127};
        String str = new String(bytes, Charsets.UTF_8);
        for (byte b : bytes) {
            int unsignedByte = Unsigneds.toUnsignedByte(b);
            byte b2 = Unsigneds.toSignedByte(unsignedByte);
            Assert.assertTrue(b == b2);
        }

        char[] chars = str.toCharArray();
        for (char c : chars) {
            int unsignedChar = Unsigneds.toUnsignedChar(c);
            char c2 = Unsigneds.toSignedChar(unsignedChar);
            Assert.assertTrue(c == c2);
        }

        short[] shorts = new short[chars.length];
        for (int i = 0; i < chars.length; i++) {
            shorts[i] = (short) chars[i];
        }
        for (short s : shorts) {
            int unsignedShort = Unsigneds.toUnsignedShort(s);
            short s2 = Unsigneds.toSignedShort(unsignedShort);
            Assert.assertTrue(s == s2);
        }
    }

}
