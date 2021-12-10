package com.jn.langx.test.util;

import com.jn.langx.util.Numbers;
import org.junit.Test;

public class NumbersTests {
    @Test
    public void castTests() {
        double a = 1.3234d;
        System.out.println(Numbers.toDouble(a));
        System.out.println(Numbers.toFloat(a));
        System.out.println(Numbers.toLong(a));
        System.out.println(Numbers.toInt(a));
    }

    @Test
    public void createIntegerTest(){
        System.out.println(Numbers.createInteger("08"));
    }

}
