package com.jn.langx.test.util;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Dates;
import com.jn.langx.util.Maths;
import com.jn.langx.util.collection.Lists;
import org.junit.Test;

import java.util.List;

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

    @Test
    public void testIsPrime() {
        long startTime = Dates.now().getTime();
        List<Integer> primes = Lists.newArrayList();
        for (int i = 1; i <= 100; i++) {
            boolean isPrime = Maths.isPrimeNumber(i);
            if (isPrime) {
                primes.add(i);
            }
        }
        long endTime = Dates.now().getTime();
        System.out.println(primes);
        System.out.println(StringTemplates.formatWithPlaceholder("cost: {} ms", endTime - startTime));
    }

    @Test
    public void testFindPrimes() {
        long startTime = Dates.now().getTime();
        List<Integer> primes = Maths.findPrimes(0, 101);
        long endTime = Dates.now().getTime();
        System.out.println(primes);
        System.out.println(StringTemplates.formatWithPlaceholder("cost: {} ms", endTime - startTime));
    }
}
