package com.jn.langx.security.gm.crypto.sm2.internal;

import com.jn.langx.security.crypto.digest.takeself.impl.SM3Digest;
import com.jn.langx.security.gm.util._utils;
import org.bouncycastle.asn1.*;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class __SM2Encryption {
    public static byte[] encrypt_der(final ECPublicKey ecPublicKey, final byte[] array, SecureRandom instance) throws Exception {
        if (instance == null) {
            instance = SecureRandom.getInstance("SHA1PRNG");
        }
        final ECParameterSpec parameters = ecPublicKey.getParameters();
        final BigInteger n = parameters.getN();
        final ECPoint g = parameters.getG();
        final BigInteger h = parameters.getH();
        byte[] kdf;
        BigInteger bigInteger;
        BigInteger bigInteger2;
        byte[] intToBytes;
        byte[] intToBytes2;
        do {
            final BigInteger gen = MyBigInteger.gen(n, instance);
            final ECPoint multiply = g.multiply(gen);
            bigInteger = multiply.getX().toBigInteger();
            bigInteger2 = multiply.getY().toBigInteger();
            final ECPoint q = ecPublicKey.getQ();
            if (q.multiply(h).isInfinity()) {
                throw new Exception("encrypt error");
            }
            final ECPoint multiply2 = q.multiply(gen);
            final BigInteger bigInteger3 = multiply2.getX().toBigInteger();
            final BigInteger bigInteger4 = multiply2.getY().toBigInteger();
            intToBytes = _utils.intToBytes(bigInteger3);
            intToBytes2 = _utils.intToBytes(bigInteger4);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(intToBytes);
            byteArrayOutputStream.write(intToBytes2);
            kdf = SM2KeyExchangeUtil.KDF(byteArrayOutputStream.toByteArray(), array.length * 8);
        } while (repeat(kdf));
        final byte[] array2 = new byte[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = (byte) (array[i] ^ kdf[i]);
        }
        final SM3Digest sm3 = new SM3Digest();
        final byte[] array3 = new byte[sm3.getDigestSize()];
        sm3.update(intToBytes, 0, intToBytes.length);
        sm3.update(array, 0, array.length);
        sm3.update(intToBytes2, 0, intToBytes2.length);
        sm3.doFinal(array3, 0);
        return new DERSequence(new ASN1Encodable[]{new ASN1Integer(bigInteger), new ASN1Integer(bigInteger2), new DEROctetString(array3), new DEROctetString(array2)}).getEncoded();
    }

    public static byte[] decrypt_der(final ECPrivateKey ecPrivateKey, final byte[] array) throws Exception {
        final ASN1Sequence instance = ASN1Sequence.getInstance(array);
        final ECParameterSpec parameters = ecPrivateKey.getParameters();
        parameters.getH();
        final BigInteger d = ecPrivateKey.getD();
        final SM3Digest sm3 = new SM3Digest();
        final BigInteger positiveValue = ((ASN1Integer) instance.getObjectAt(0)).getPositiveValue();
        final BigInteger positiveValue2 = ((ASN1Integer) instance.getObjectAt(1)).getPositiveValue();
        final byte[] octets = ((ASN1OctetString) instance.getObjectAt(3)).getOctets();
        final byte[] octets2 = ((ASN1OctetString) instance.getObjectAt(2)).getOctets();
        final ECPoint point = parameters.getCurve().createPoint(positiveValue, positiveValue2, false);
        final ECPoint multiply = point.multiply(d);
        final BigInteger bigInteger = multiply.getX().toBigInteger();
        final BigInteger bigInteger2 = multiply.getY().toBigInteger();
        final byte[] intToBytes = _utils.intToBytes(bigInteger);
        final byte[] intToBytes2 = _utils.intToBytes(bigInteger2);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(intToBytes);
        byteArrayOutputStream.write(intToBytes2);
        final byte[] kdf = SM2KeyExchangeUtil.KDF(byteArrayOutputStream.toByteArray(), octets.length * 8);
        if (repeat(kdf)) {
            throw new RuntimeException("error cipher text(02)");
        }
        final byte[] array2 = new byte[octets.length];
        for (int i = 0; i < array2.length; ++i) {
            array2[i] = (byte) (octets[i] ^ kdf[i]);
        }
        sm3.update(intToBytes, 0, intToBytes.length);
        sm3.update(array2, 0, array2.length);
        sm3.update(intToBytes2, 0, intToBytes2.length);
        final byte[] array3 = new byte[sm3.getDigestSize()];
        sm3.doFinal(array3, 0);
        final boolean equals = Arrays.equals(array3, octets2);
        if (!equals) {
            throw new RuntimeException("error cipher text(03)");
        }
        return array2;
    }

    public static boolean repeat(final byte[] array) {
        boolean b = true;
        for (final byte b2 : array) {
            b = (b && b2 == 0);
            if (!b) {
                break;
            }
        }
        return b;
    }
}
