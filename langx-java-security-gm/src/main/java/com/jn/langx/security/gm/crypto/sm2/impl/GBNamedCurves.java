package com.jn.langx.security.gm.crypto.sm2.impl;


import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9ECParametersHolder;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.Strings;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class GBNamedCurves {
    private static final Map<String, ASN1ObjectIdentifier> objIds;
    static final Map<ASN1ObjectIdentifier, String> names;
    private static final Map<ASN1ObjectIdentifier, X9ECParametersHolder> curves;
    private static X9ECParametersHolder sm2_256;

    static {
        objIds = new HashMap<String, ASN1ObjectIdentifier>();
        names = new HashMap<ASN1ObjectIdentifier, String>();
        curves = new HashMap<ASN1ObjectIdentifier, X9ECParametersHolder>();
        GBNamedCurves.sm2_256 = new X9ECParametersHolder() {
            @Override
            protected X9ECParameters createParameters() {
                final ECCurve.Fp fp = new ECCurve.Fp(new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF", 16), new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC", 16), new BigInteger("28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93", 16));
                return new X9ECParameters(fp, fp.createPoint(new BigInteger("32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16), new BigInteger("BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16), false), new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", 16));
            }
        };
        defineCurve("sm2_256", new ASN1ObjectIdentifier("1.2.156.10197.1.301"), GBNamedCurves.sm2_256);
    }

    private static void defineCurve(final String s, final ASN1ObjectIdentifier asn1ObjectIdentifier, final X9ECParametersHolder x9ECParametersHolder) {
        GBNamedCurves.objIds.put(s, asn1ObjectIdentifier);
        GBNamedCurves.names.put(asn1ObjectIdentifier, s);
        GBNamedCurves.curves.put(asn1ObjectIdentifier, x9ECParametersHolder);
    }

    public static ASN1ObjectIdentifier getOID(final String s) {
        return GBNamedCurves.objIds.get(Strings.toLowerCase(s));
    }

    public static X9ECParameters getByOID(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final X9ECParametersHolder x9ECParametersHolder = GBNamedCurves.curves.get(asn1ObjectIdentifier);
        if (x9ECParametersHolder != null) {
            return x9ECParametersHolder.getParameters();
        }
        return null;
    }

    public static String getName(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        return GBNamedCurves.names.get(asn1ObjectIdentifier);
    }
}
