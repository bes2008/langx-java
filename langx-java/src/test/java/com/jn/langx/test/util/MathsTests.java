package com.jn.langx.test.util;

import com.jn.langx.util.Maths;
import org.junit.Test;

public class MathsTests {
    @Test
    public void test() {
        System.out.println(Maths.formatPrecision(123123.20, 0));
        System.out.println(Maths.formatPrecision(123123.21, 0));


        System.out.println(Maths.formatPrecision(123123, 2));
        System.out.println(Maths.formatPrecision(123123.0, 2));
        System.out.println(Maths.formatPrecision(123123.2, 2));
        System.out.println(Maths.formatPrecision(123123.20, 2));
        System.out.println(Maths.formatPrecision(123123.21, 2));
        System.out.println(Maths.formatPrecision(123123.2302343234234d, 2));
        System.out.println(Maths.formatPrecision(123123.2312343234234d, 2));
        System.out.println(Maths.formatPrecision(123123.2342343234234d, 2));
        System.out.println(Maths.formatPrecision(123123.2352343234234d, 2));
        System.out.println(Maths.formatPrecision(123123.2362343234234d, 2));
        System.out.println(Maths.formatPrecision(123123.2392343234234d, 2));
    }
}
