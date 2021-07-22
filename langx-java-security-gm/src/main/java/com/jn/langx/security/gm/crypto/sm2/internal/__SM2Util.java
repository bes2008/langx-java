package com.jn.langx.security.gm.crypto.sm2.internal;


import com.jn.langx.security.gm.GmJceProvider;
import com.jn.langx.security.crypto.digest.internal.impl._SM3Digest;
import com.jn.langx.security.gm.crypto.sm2.SM2ParameterSpec;
import com.jn.langx.security.gm.util._utils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.*;

/**
 * @see // cn.gmssl.crypto.impl.sm2
 */
public class __SM2Util {
    private static final String DEFAULT_SM2_ID = "1234567812345678";
    public static byte[] Z(byte[] bytes, final ECPublicKey ecPublicKey, final Digest digest) {
        if (bytes == null) {
            bytes = DEFAULT_SM2_ID.getBytes();
        }
        final int n = bytes.length * 8;
        final ECCurve curve = ecPublicKey.getParameters().getCurve();
        final BigInteger bigInteger = curve.getA().toBigInteger();
        final BigInteger bigInteger2 = curve.getB().toBigInteger();
        final ECPoint g = ecPublicKey.getParameters().getG();
        final BigInteger bigInteger3 = g.getX().toBigInteger();
        final BigInteger bigInteger4 = g.getY().toBigInteger();
        final ECPoint q = ecPublicKey.getQ();
        final BigInteger bigInteger5 = q.getX().toBigInteger();
        final BigInteger bigInteger6 = q.getY().toBigInteger();
        final byte[] array = {(byte) (n >> 8), (byte) n};
        int m = 0;
        if (curve instanceof ECCurve.F2m) {
            m = ((ECCurve.F2m) curve).getM();
        }
        final byte[] intToBytes = _utils.intToBytes(bigInteger, m);
        final byte[] intToBytes2 = _utils.intToBytes(bigInteger2, m);
        final byte[] intToBytes3 = _utils.intToBytes(bigInteger3, m);
        final byte[] intToBytes4 = _utils.intToBytes(bigInteger4, m);
        final byte[] intToBytes5 = _utils.intToBytes(bigInteger5, m);
        final byte[] intToBytes6 = _utils.intToBytes(bigInteger6, m);
        final _SM3Digest sm3 = new _SM3Digest();
        sm3.update(array, 0, array.length);
        sm3.update(bytes, 0, bytes.length);
        sm3.update(intToBytes, 0, intToBytes.length);
        sm3.update(intToBytes2, 0, intToBytes2.length);
        sm3.update(intToBytes3, 0, intToBytes3.length);
        sm3.update(intToBytes4, 0, intToBytes4.length);
        sm3.update(intToBytes5, 0, intToBytes5.length);
        sm3.update(intToBytes6, 0, intToBytes6.length);
        final byte[] array2 = new byte[sm3.getDigestSize()];
        sm3.doFinal(array2, 0);
        digest.update(array2, 0, array2.length);
        return array2;
    }

    public static byte[] Z(byte[] bytes, final ECPublicKey ecPublicKey, final MessageDigest messageDigest) throws Exception {
        if (bytes == null) {
            bytes = DEFAULT_SM2_ID.getBytes();
        }
        final int n = bytes.length * 8;
        final ECCurve curve = ecPublicKey.getParameters().getCurve();
        final BigInteger bigInteger = curve.getA().toBigInteger();
        final BigInteger bigInteger2 = curve.getB().toBigInteger();
        final ECPoint g = ecPublicKey.getParameters().getG();
        final BigInteger bigInteger3 = g.getX().toBigInteger();
        final BigInteger bigInteger4 = g.getY().toBigInteger();
        final ECPoint q = ecPublicKey.getQ();
        final BigInteger bigInteger5 = q.getX().toBigInteger();
        final BigInteger bigInteger6 = q.getY().toBigInteger();
        final byte[] array = {(byte) (n >> 8), (byte) n};
        int m = 0;
        if (curve instanceof ECCurve.F2m) {
            m = ((ECCurve.F2m) curve).getM();
        }
        final byte[] intToBytes = _utils.intToBytes(bigInteger, m);
        final byte[] intToBytes2 = _utils.intToBytes(bigInteger2, m);
        final byte[] intToBytes3 = _utils.intToBytes(bigInteger3, m);
        final byte[] intToBytes4 = _utils.intToBytes(bigInteger4, m);
        final byte[] intToBytes5 = _utils.intToBytes(bigInteger5, m);
        final byte[] intToBytes6 = _utils.intToBytes(bigInteger6, m);
        final MessageDigest instance = MessageDigest.getInstance("SM3");
        instance.update(array, 0, array.length);
        instance.update(bytes, 0, bytes.length);
        instance.update(intToBytes, 0, intToBytes.length);
        instance.update(intToBytes2, 0, intToBytes2.length);
        instance.update(intToBytes3, 0, intToBytes3.length);
        instance.update(intToBytes4, 0, intToBytes4.length);
        instance.update(intToBytes5, 0, intToBytes5.length);
        instance.update(intToBytes6, 0, intToBytes6.length);
        final byte[] digest = instance.digest();
        messageDigest.update(digest);
        return digest;
    }

    public static byte[] Z(byte[] bytes, final java.security.interfaces.ECPublicKey ecPublicKey, final MessageDigest messageDigest) throws Exception {
        if (bytes == null) {
            bytes = DEFAULT_SM2_ID.getBytes();
        }
        final int n = bytes.length * 8;
        final EllipticCurve curve = ecPublicKey.getParams().getCurve();
        final BigInteger a = curve.getA();
        final BigInteger b = curve.getB();
        final java.security.spec.ECPoint generator = ecPublicKey.getParams().getGenerator();
        final BigInteger affineX = generator.getAffineX();
        final BigInteger affineY = generator.getAffineY();
        final java.security.spec.ECPoint w = ecPublicKey.getW();
        final BigInteger affineX2 = w.getAffineX();
        final BigInteger affineY2 = w.getAffineY();
        final byte[] array = {(byte) (n >> 8), (byte) n};
        final int n2 = 0;
        final byte[] intToBytes = _utils.intToBytes(a, n2);
        final byte[] intToBytes2 = _utils.intToBytes(b, n2);
        final byte[] intToBytes3 = _utils.intToBytes(affineX, n2);
        final byte[] intToBytes4 = _utils.intToBytes(affineY, n2);
        final byte[] intToBytes5 = _utils.intToBytes(affineX2, n2);
        final byte[] intToBytes6 = _utils.intToBytes(affineY2, n2);
        final MessageDigest instance = MessageDigest.getInstance("SM3");
        instance.update(array, 0, array.length);
        instance.update(bytes, 0, bytes.length);
        instance.update(intToBytes, 0, intToBytes.length);
        instance.update(intToBytes2, 0, intToBytes2.length);
        instance.update(intToBytes3, 0, intToBytes3.length);
        instance.update(intToBytes4, 0, intToBytes4.length);
        instance.update(intToBytes5, 0, intToBytes5.length);
        instance.update(intToBytes6, 0, intToBytes6.length);
        final byte[] digest = instance.digest();
        messageDigest.update(digest);
        return digest;
    }

    public static ECParameterSpec getSM2ParamSpec() {
        final ECCurve.Fp fp = new ECCurve.Fp(_utils.sm2_p, _utils.sm2_a, _utils.sm2_b);
        return new ECParameterSpec(fp, fp.createPoint(_utils.sm2_xG, _utils.sm2_yG, false), _utils.sm2_n);
    }

    public static java.security.spec.ECParameterSpec getStandardECParamSpec() {
        return new java.security.spec.ECParameterSpec(new EllipticCurve(new ECFieldFp(_utils.sm2_p), _utils.sm2_a, _utils.sm2_b), new java.security.spec.ECPoint(_utils.sm2_xG, _utils.sm2_yG), _utils.sm2_n, 1);
    }

    public static java.security.spec.ECParameterSpec getStandardECParamSpec_f2m() {
        return new java.security.spec.ECParameterSpec(new EllipticCurve(new ECFieldF2m(_utils.sm2_m, new int[]{_utils.sm2_k}), _utils.sm2_a, _utils.sm2_b), new java.security.spec.ECPoint(_utils.sm2_xG, _utils.sm2_yG), _utils.sm2_n, 1);
    }

    public static byte[] encodePoint(final ECPoint ecPoint) {
        final BigInteger bigInteger = ecPoint.getX().toBigInteger();
        final BigInteger bigInteger2 = ecPoint.getY().toBigInteger();
        final StdDSAEncoder stdDSAEncoder = new StdDSAEncoder();
        byte[] encode;
        try {
            encode = stdDSAEncoder.encode(bigInteger, bigInteger2);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return encode;
    }

    public static byte[] getId(final X509Certificate x509Certificate, final int n) {
        try {
            return DEFAULT_SM2_ID.getBytes();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Signature sm2Sign(final PrivateKey privateKey, final PublicKey publicKey) {
        try {
            final Signature instance = Signature.getInstance("SM3withSM2");
            final String name = instance.getProvider().getName();
            if (GmJceProvider.NAME.equals(name)) {
                instance.setParameter(new SM2ParameterSpec(DEFAULT_SM2_ID.getBytes(), publicKey));
            }
            instance.initSign(privateKey);
            return instance;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ECPublicKey toTsgECPublicKey(final PublicKey publicKey) throws Exception {
        if (!(publicKey instanceof java.security.interfaces.ECPublicKey)) {
            return (ECPublicKey) publicKey;
        }
        return (ECPublicKey) KeyFactory.getInstance("SM2", GmJceProvider.NAME).generatePublic(new ECPublicKeySpec(((java.security.interfaces.ECPublicKey) publicKey).getW(), getStandardECParamSpec()));
    }

    public static ECPrivateKey toTsgECPrivateKey(final PrivateKey privateKey) throws Exception {
        if (!(privateKey instanceof java.security.interfaces.ECPrivateKey)) {
            return (ECPrivateKey) privateKey;
        }
        return (ECPrivateKey) KeyFactory.getInstance("SM2", GmJceProvider.NAME).generatePrivate(new ECPrivateKeySpec(((java.security.interfaces.ECPrivateKey) privateKey).getS(), getStandardECParamSpec()));
    }
}
