package com.jn.langx.security.crypto.keyspec.der;

import com.jn.langx.security.crypto.keyspec.PrivateKeySpecParser;
import com.jn.langx.util.Throwables;

import java.math.BigInteger;
import java.security.spec.RSAPrivateCrtKeySpec;

public class RsaPkcs1PrivateKeySpecParser implements PrivateKeySpecParser<RSAPrivateCrtKeySpec> {
    @Override
    public RSAPrivateCrtKeySpec get(byte[] derEncodedBytes) {
        try {
            DerParser parser = new DerParser(derEncodedBytes);
            DerParser.Asn1Object sequence = parser.readAsn1Object();
            parser = sequence.getParser();
            parser.readAsn1Object().getInteger(); // (version) We don't need it but must read to get to modulus
            BigInteger modulus = parser.readAsn1Object().getInteger();
            BigInteger publicExponent = parser.readAsn1Object().getInteger();
            BigInteger privateExponent = parser.readAsn1Object().getInteger();
            BigInteger prime1 = parser.readAsn1Object().getInteger();
            BigInteger prime2 = parser.readAsn1Object().getInteger();
            BigInteger exponent1 = parser.readAsn1Object().getInteger();
            BigInteger exponent2 = parser.readAsn1Object().getInteger();
            BigInteger coefficient = parser.readAsn1Object().getInteger();
            return new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, prime1, prime2, exponent1, exponent2, coefficient);
        }catch (Throwable ex){
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }
}
