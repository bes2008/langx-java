package com.jn.langx.security.gm.crypto.sm2.internal;


import com.jn.langx.codec.hex.Hex;
import com.jn.langx.security.gm.crypto.sm3.internal._SM3DigestImpl;
import com.jn.langx.security.gm.util.MyBigInteger;
import com.jn.langx.security.gm.util._utils;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.math.ec.ECPoint;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

public class SM2KeyExchangeUtil {
    public static BigInteger generateRandom(final ECPublicKey ecPublicKey, final SecureRandom secureRandom) {
        return generateRandom(ecPublicKey.getParameters().getN(), secureRandom);
    }

    public static BigInteger generateRandom(final BigInteger bigInteger, final SecureRandom secureRandom) {
        return MyBigInteger.gen(bigInteger, secureRandom);
    }

    public static ECPoint generateR(final ECPublicKey ecPublicKey, final BigInteger bigInteger) {
        return ecPublicKey.getParameters().getG().multiply(bigInteger);
    }

    public static byte[] generateK(final ECPublicKey ecPublicKey, final ECPrivateKey ecPrivateKey, final ECPublicKey ecPublicKey2, final BigInteger bigInteger, final ECPoint ecPoint, final byte[] array, final byte[] array2, final boolean b, final int n) throws Exception {
        return caculateK(ecPublicKey, ecPrivateKey, ecPublicKey2, generateR(ecPublicKey, bigInteger), ecPoint, bigInteger, array, array2, b, n);
    }

    public static byte[] caculateK(final ECPublicKey ecPublicKey, final ECPrivateKey ecPrivateKey, final ECPublicKey ecPublicKey2, final ECPoint ecPoint, final ECPoint ecPoint2, final BigInteger bigInteger, final byte[] array, final byte[] array2, final boolean b, final int n) throws Exception {
        final BigInteger n2 = ecPublicKey.getParameters().getN();
        final BigInteger shiftLeft = BigInteger.ONE.shiftLeft((int) (Math.ceil(n2.subtract(BigInteger.ONE).bitLength() / 2) - 1.0));
        final BigInteger add = shiftLeft.add(ecPoint.getX().toBigInteger().and(shiftLeft.subtract(BigInteger.ONE)));
        final BigInteger mod = ecPrivateKey.getD().add(add.multiply(bigInteger)).mod(n2);
        final BigInteger add2 = shiftLeft.add(ecPoint2.getX().toBigInteger().and(shiftLeft.subtract(BigInteger.ONE)));
        final ECPoint multiply = ecPublicKey2.getQ().add(ecPoint2.multiply(add2)).multiply(ecPublicKey.getParameters().getH().multiply(mod));
        final BigInteger bigInteger2 = multiply.getX().toBigInteger();
        final BigInteger bigInteger3 = multiply.getY().toBigInteger();
        final _SM3DigestImpl sm3 = new _SM3DigestImpl();
        final byte[] z = __SM2Util.Z(array, ecPublicKey, sm3);
        final byte[] z2 = __SM2Util.Z(array2, ecPublicKey2, sm3);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(_utils.intToBytes(bigInteger2));
            byteArrayOutputStream.write(_utils.intToBytes(bigInteger3));
            if (b) {
                byteArrayOutputStream.write(z);
                byteArrayOutputStream.write(z2);
            } else {
                byteArrayOutputStream.write(z2);
                byteArrayOutputStream.write(z);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return KDF(byteArrayOutputStream.toByteArray(), n * 8);
    }

    public static byte[] caculateK_debug(final ECPublicKey ecPublicKey, final ECPrivateKey ecPrivateKey, final ECPublicKey ecPublicKey2, final ECPoint ecPoint, final ECPoint ecPoint2, final BigInteger bigInteger, final byte[] array, final byte[] array2, final boolean b, final StringBuilder sb) throws Exception {
        final BigInteger n = ecPublicKey.getParameters().getN();
        final BigInteger shiftLeft = BigInteger.ONE.shiftLeft((int) (Math.ceil(n.subtract(BigInteger.ONE).bitLength() / 2) - 1.0));
        final BigInteger add = shiftLeft.add(ecPoint.getX().toBigInteger().and(shiftLeft.subtract(BigInteger.ONE)));
        final BigInteger mod = ecPrivateKey.getD().add(add.multiply(bigInteger)).mod(n);
        final BigInteger add2 = shiftLeft.add(ecPoint2.getX().toBigInteger().and(shiftLeft.subtract(BigInteger.ONE)));
        final ECPoint multiply = ecPublicKey2.getQ().add(ecPoint2.multiply(add2)).multiply(ecPublicKey.getParameters().getH().multiply(mod));
        final BigInteger bigInteger2 = multiply.getX().toBigInteger();
        final BigInteger bigInteger3 = multiply.getY().toBigInteger();
        final _SM3DigestImpl sm3 = new _SM3DigestImpl();
        final byte[] z = __SM2Util.Z(array, ecPublicKey, sm3);
        final byte[] z2 = __SM2Util.Z(array2, ecPublicKey2, sm3);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(_utils.intToBytes(bigInteger2));
            byteArrayOutputStream.write(_utils.intToBytes(bigInteger3));
            if (b) {
                byteArrayOutputStream.write(z);
                byteArrayOutputStream.write(z2);
            } else {
                byteArrayOutputStream.write(z2);
                byteArrayOutputStream.write(z);
            }
        } catch (Exception ex) {
            throw ex;
        }
        final byte[] kdf = KDF(byteArrayOutputStream.toByteArray(), 384);
        sb.append("caculateK-publicKey:" + ecPublicKey + "\n");
        sb.append("caculateK-privateKey:" + ecPrivateKey + "\n");
        sb.append("caculateK-publicKeyRemote:" + ecPublicKey2 + "\n");
        sb.append("caculateK-RLocal.X:" + ecPoint.getX().toBigInteger().toString(16) + "\n");
        sb.append("caculateK-RLocal.Y:" + ecPoint.getY().toBigInteger().toString(16) + "\n");
        sb.append("caculateK-RRemote.X:" + ecPoint2.getX().toBigInteger().toString(16) + "\n");
        sb.append("caculateK-RRemote.Y:" + ecPoint2.getY().toBigInteger().toString(16) + "\n");
        sb.append("caculateK-randomLocal" + bigInteger.toString(16) + "\n");
        sb.append("caculateK-idLocal:" + Hex.encodeHexString(array) + "\n");
        sb.append("caculateK-x:" + add.toString(16) + "\n");
        sb.append("caculateK-t:" + mod.toString(16) + "\n");
        sb.append("caculateK-x2:" + add2.toString(16) + "\n");
        sb.append("caculateK-xU-bytes:" + Hex.encodeHexString(_utils.intToBytes(bigInteger2)) + "\n");
        sb.append("caculateK-xU-length:" + _utils.intToBytes(bigInteger2).length + "\n");
        sb.append("caculateK-yU-bytes:" + Hex.encodeHexString(_utils.intToBytes(bigInteger3)) + "\n");
        sb.append("caculateK-yU-length:" + _utils.intToBytes(bigInteger3).length + "\n");
        sb.append("caculateK-ZLocal:" + Hex.encodeHexString(z) + "\n");
        sb.append("caculateK-ZRemote:" + Hex.encodeHexString(z2) + "\n");
        sb.append("caculateK-K:" + kdf + "\n");
        return kdf;
    }

    public static byte[] KDF(final byte[] array, final int n) {
        final _SM3DigestImpl sm3 = new _SM3DigestImpl();
        final int n2 = sm3.getDigestSize() * 8;
        final byte[] array2 = new byte[n / 8];
        int i;
        int n3;
        for (i = 0, n3 = 1; i < n / n2; ++i, ++n3) {
            sm3.update(array, 0, array.length);
            sm3.update((byte) (n3 >> 24 & 0xFF));
            sm3.update((byte) (n3 >> 16 & 0xFF));
            sm3.update((byte) (n3 >> 8 & 0xFF));
            sm3.update((byte) (n3 >> 0 & 0xFF));
            sm3.doFinal(array2, i * n2 / 8);
        }
        if (n % n2 != 0) {
            final byte[] array3 = new byte[sm3.getDigestSize()];
            sm3.update(array, 0, array.length);
            sm3.update((byte) (n3 >> 24 & 0xFF));
            sm3.update((byte) (n3 >> 16 & 0xFF));
            sm3.update((byte) (n3 >> 8 & 0xFF));
            sm3.update((byte) (n3 >> 0 & 0xFF));
            sm3.doFinal(array3, 0);
            final int n4 = i * n2 / 8;
            System.arraycopy(array3, 0, array2, n4, n / 8 - n4);
        }
        return array2;
    }
}