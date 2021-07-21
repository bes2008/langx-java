package com.jn.langx.security.gm.crypto.sm2.impl;

import org.bouncycastle.asn1.*;
import org.bouncycastle.jcajce.provider.asymmetric.util.DSAEncoder;

import java.io.IOException;
import java.math.BigInteger;

public class StdDSAEncoder implements DSAEncoder {
    @Override
    public byte[] encode(final BigInteger bigInteger, final BigInteger bigInteger2) throws IOException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new DERInteger(bigInteger));
        asn1EncodableVector.add(new DERInteger(bigInteger2));
        return new DERSequence(asn1EncodableVector).getEncoded("DER");
    }

    @Override
    public BigInteger[] decode(final byte[] array) throws IOException {
        final ASN1Sequence asn1Sequence = (ASN1Sequence) ASN1Primitive.fromByteArray(array);
        return new BigInteger[]{((DERInteger) asn1Sequence.getObjectAt(0)).getValue(), ((DERInteger) asn1Sequence.getObjectAt(1)).getValue()};
    }
}
