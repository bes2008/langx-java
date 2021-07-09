package com.jn.langx.security.privatekey.keyspec.parser;

import com.jn.langx.security.cert.DerParser;
import com.jn.langx.util.Throwables;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;

public class EcPrivateKeySpecParser implements PrivateKeySpecParser<ECPrivateKeySpec>{
    @Override
    public ECPrivateKeySpec get(byte[] derEncodedBytes) {
        try {
            DerParser parser = new DerParser(derEncodedBytes);
            DerParser.Asn1Object sequence = parser.readAsn1Object();
            parser = sequence.getParser();
            parser.readAsn1Object().getInteger(); // version
            String keyHex = parser.readAsn1Object().getString();
            BigInteger privateKeyInt = new BigInteger(keyHex, 16);
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            AlgorithmParameterSpec prime256v1ParamSpec = new ECGenParameterSpec("secp256r1");
            keyPairGenerator.initialize(prime256v1ParamSpec);
            ECParameterSpec parameterSpec = ((ECKey) keyPairGenerator.generateKeyPair().getPrivate()).getParams();
            return new ECPrivateKeySpec(privateKeyInt, parameterSpec);
        }catch (Throwable ex){
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }
}
