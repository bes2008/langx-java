package com.jn.langx.test.util;

import org.junit.Test;

public class RadixsTest {
    /**
     * All possible chars for representing a number as a String
     */
    final static char[] digits = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    @Test
    public void test() {
        Integer i = new Integer(100);
        System.out.println(Integer.toBinaryString(i));
        System.out.println(Integer.toOctalString(i));
        System.out.println(Integer.toHexString(i));

        System.out.println(Character.digit(i, 2));
        System.out.println(Character.digit(i, 8));
        System.out.println(Character.digit(i, 16));

    }
}
