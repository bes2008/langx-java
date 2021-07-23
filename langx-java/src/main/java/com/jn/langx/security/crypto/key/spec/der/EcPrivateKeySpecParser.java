package com.jn.langx.security.crypto.key.spec.der;

import com.jn.langx.security.crypto.key.spec.PrivateKeySpecParser;
import com.jn.langx.util.Throwables;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;

/**
 *
 * Key-Length:
 * 192-bit (curve secp192r1),
 * 233-bit (curve sect233k1),
 * 224-bit (curve secp224k1),
 * 256-bit (curves secp256k1 and Curve25519),
 * 283-bit (curve sect283k1),
 * 384-bit (curves p384 and secp384r1),
 * 409-bit (curve sect409r1),
 * 414-bit (curve Curve41417),
 * 448-bit (curve Curve448-Goldilocks),
 * 511-bit (curve M-511),
 * 521-bit (curve P-521),
 * 571-bit (curve sect571k1)
 * and many others.
 */
public class EcPrivateKeySpecParser implements PrivateKeySpecParser<ECPrivateKeySpec> {
    @Override
    public ECPrivateKeySpec parse(byte[] derEncodedBytes) {
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
