package com.jn.langx.security.bc.util;

import java.math.BigInteger;
import java.util.Random;

public class MyBigInteger {
    public static BigInteger gen(final BigInteger bigInteger, final Random random) {
        final int bitLength = bigInteger.bitLength();
        final int n = bitLength / 8;
        final int n2 = 64;
        int n3 = 0;
        BigInteger bigInteger2;
        while (true) {
            bigInteger2 = new BigInteger(bitLength, random);
            if (!bigInteger2.equals(BigInteger.ZERO) && bigInteger2.compareTo(bigInteger) < 0) {
                if (bigInteger2.bitLength() / 8 != n) {
                    continue;
                }
                if (++n3 > n2) {
                    break;
                }
                continue;
            }
        }
        return bigInteger2;
    }
}
