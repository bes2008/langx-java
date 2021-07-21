package com.jn.langx.security.gm.util;


import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;

import java.math.BigInteger;

public class _utils {

    public static BigInteger sm2_p;
    public static BigInteger sm2_a;
    public static BigInteger sm2_b;
    public static BigInteger sm2_xG;
    public static BigInteger sm2_yG;
    public static BigInteger sm2_n;
    public static int sm2_m;
    public static int sm2_k;
    public static String sm2_OID_SM3WITHSM2;
    public static String sm2_OID_SM2_PUBLICKEY;
    public static String sm2_OID_SM2_256CURVE;

    static {
        sm2_p = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF", 16);
        sm2_a = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC", 16);
        sm2_b = new BigInteger("28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93", 16);
        sm2_xG = new BigInteger("32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
        sm2_yG = new BigInteger("BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);
        sm2_n = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", 16);
        sm2_m = 257;
        sm2_k = 12;
        sm2_OID_SM3WITHSM2 = "1.2.156.10197.1.501";
        sm2_OID_SM2_PUBLICKEY = "1.2.840.10045.2.1";
        sm2_OID_SM2_256CURVE = "1.2.156.10197.1.301";
    }
    public static ECParameterSpec getSM2NamedCuve() {
        final ECCurve.Fp fp = new ECCurve.Fp(_utils.sm2_p, _utils.sm2_a, _utils.sm2_b);
        return new ECNamedCurveParameterSpec(_utils.sm2_OID_SM2_256CURVE, fp, fp.createPoint(_utils.sm2_xG, _utils.sm2_yG, false), _utils.sm2_n);
    }
    public static byte[] intToBytes(final BigInteger bigInteger, final int n) {
        if (n == 0) {
            return intToBytes(bigInteger);
        }
        final int n2 = n / 8;
        final int n3 = (n % 8 == 0) ? n2 : (n2 + 1);
        final byte[] intToBytes = intToBytes(bigInteger);
        if (intToBytes.length == n3) {
            return intToBytes;
        }
        final int n4 = n3 - intToBytes.length;
        final byte[] array = new byte[n3];
        System.arraycopy(intToBytes, 0, array, n4, intToBytes.length);
        return array;
    }

    public static byte[] intToBytes(final BigInteger bigInteger) {
        byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length < 32) {
            final byte[] array = new byte[32];
            System.arraycopy(byteArray, 0, array, 32 - byteArray.length, byteArray.length);
            byteArray = array;
        } else if (byteArray.length > 32) {
            final byte[] array2 = new byte[byteArray.length - (byteArray.length - 32)];
            System.arraycopy(byteArray, byteArray.length - 32, array2, 0, array2.length);
            byteArray = array2;
        }
        return byteArray;
    }

}
