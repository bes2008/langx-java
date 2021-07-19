package com.jn.langx.security.crypto.key.spec.der;

import com.jn.langx.security.crypto.key.spec.PrivateKeySpecParser;
import com.jn.langx.util.Throwables;

import java.math.BigInteger;
import java.security.spec.DSAPrivateKeySpec;

public class DsaPrivateKeySpecParser implements PrivateKeySpecParser<DSAPrivateKeySpec> {
    @Override
    public DSAPrivateKeySpec get(byte[] derEncodedBytes) {
        try {
            DerParser parser = new DerParser(derEncodedBytes);
            DerParser.Asn1Object sequence = parser.readAsn1Object();
            parser = sequence.getParser();
            parser.readAsn1Object().getInteger(); // (version) We don't need it but must read to get to p
            BigInteger p = parser.readAsn1Object().getInteger();
            BigInteger q = parser.readAsn1Object().getInteger();
            BigInteger g = parser.readAsn1Object().getInteger();
            parser.readAsn1Object().getInteger(); // we don't need x
            BigInteger x = parser.readAsn1Object().getInteger();
            return new DSAPrivateKeySpec(x, p, q, g);
        }catch (Throwable ex){
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }
}
